package org.example.zentrio.service.impl;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.zentrio.dto.request.AssignedRoleRequest;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.MemberResponse;
import org.example.zentrio.enums.ImageExtension;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.*;
import org.example.zentrio.repository.*;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.EmailService;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;
    private final MinioClient minioClient;
    private final WorkspaceService workspaceService;
    private final EmailService emailService;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public Board createBoard(BoardRequest boardRequest) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Workspace workspace = workspaceRepository.getWorkspaceById(boardRequest.getWorkspaceId(), userId);
        if (workspace == null) {
            throw new NotFoundException("Workspace id " + boardRequest.getWorkspaceId() + " not found");
        }
        Board board = boardRepository.createBoard(boardRequest, boardRequest.getWorkspaceId());
        UUID boardId = board.getBoardId();
        UUID roleId = roleRepository.getRoleIdByRoleName(RoleName.ROLE_MANAGER.toString());
        boardRepository.insertMember(userId, boardId, roleId);
        return board;
    }

    @Override
    public ApiResponse<HashSet<Board>> getAllBoardsByWorkspaceId(UUID workspaceId, Integer page, Integer size) {
        workspaceService.getWorkspaceById(workspaceId);

        Integer offset = (page - 1) * size;

        List<Board> boardList = boardRepository.getAllBoardsByWorkspaceId(workspaceId, size, offset);

        HashSet<Board> boards = new HashSet<>(boardList);

        int totalElements = boardRepository.getBoardCountByWorkspaceId(workspaceId);
        int totalPages = (int) Math.ceil(totalElements / (double) size);

        return ApiResponse.<HashSet<Board>>builder()
                .success(true)
                .message("Get all tasks successfully")
                .payload(boards)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .pagination(new Pagination(page, totalElements, totalPages))
                .build();
    }


    @Override
    public Board updateBoardByBoardId(BoardRequest boardRequest, UUID boardId) {
        Workspace workspace = workspaceRepository.getWorkspaceByWorkspaceId(boardRequest.getWorkspaceId());
        if (workspace == null) {
            throw new NotFoundException("Workspace id " + boardRequest.getWorkspaceId() + " not found");
        }
        getBoardByBoardId(boardId);
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);

        if (roleName == null) {
            throw new NotFoundException("You're doesn't have a ROLE MANGER to update this board");
        }

        if (!roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new NotFoundException("You're doesn't have permission to update this board");
        }

        return boardRepository.updateBoardByBoardId(boardRequest, boardId);
    }

    @Override
    public void deleteBoardByBoardId(UUID boardId, UUID workspaceId) {
        getBoardByBoardId(boardId);
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);

        if (roleName == null) {
            throw new NotFoundException("You're doesn't have a ROLE MANGER to deleted this board");
        }

        if (!roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new NotFoundException("You're doesn't have permission to deleted this board");
        }
        boardRepository.deleteBoardByBoardId(boardId, workspaceId);
    }

    @Override
    public Board getBoardByBoardId(UUID boardId) {
        Board board = boardRepository.getBoardByBoardId(boardId);

        if (board == null) {
            throw new NotFoundException("Board id " + boardId + " not found");
        }

        return board;
    }

    @Override
    public HashSet<Board> getBoardByBoardTitle(@RequestBody String boardTitle) {
        List<Board> boardList = boardRepository.getBoardByTitle(boardTitle);
        if (boardList == null) {
            throw new NotFoundException("Board title " + boardTitle + " not found");
        }
        return new HashSet<>(boardList);
    }


    @Override
    public HashSet<MemberResponse> getBoardByBoardIdWithMember(UUID boardId) {
        Board board = boardRepository.getBoardByBoardId(boardId);
        if (board == null) {
            throw new NotFoundException("Board id " + boardId + " not found");
        }
        return boardRepository.getBoardByBoardIdWithMember(boardId);
    }

    @Override
    public void assignRoleToBoard(AssignedRoleRequest assignedRoleRequest) {
        getBoardByBoardId(assignedRoleRequest.getBoardId());
        validateCurrentUserRoles(assignedRoleRequest.getBoardId());
        Workspace workspace = workspaceRepository.getWorkspaceByWorkspaceId(assignedRoleRequest.getWorkspaceId());
        if (workspace == null) {
            throw new NotFoundException("Workspace id not found");
        }
        AppUser userId = appUserRepository.getUserById(assignedRoleRequest.getAssigneeId());
        if (userId == null) {
            throw new NotFoundException("Assignee ID not found");
        }

        UUID existingMemberId = boardRepository.getMemberIdByUserIdAndBoardId(assignedRoleRequest.getAssigneeId(), assignedRoleRequest.getBoardId());

        if (existingMemberId != null) {
            throw new ConflictException("Member is already assigned");
        }

        UUID roleId = roleRepository.getRoleIdByRoleName(assignedRoleRequest.getRoleName().toString());

        roleRepository.insertToMember(assignedRoleRequest.getAssigneeId(), assignedRoleRequest.getBoardId(), roleId);
    }

    @SneakyThrows
    @Override
    public FileMetadata uploadBoardImage(UUID boardId, MultipartFile file) {
        getBoardByBoardId(boardId);
        List<String> imageExtensions = new ArrayList<>();

        for (ImageExtension imageExtension : ImageExtension.values()) {
            imageExtensions.add(imageExtension.getExtension());
        }

        if (!imageExtensions.contains(StringUtils.getFilenameExtension(file.getOriginalFilename()))) {
            throw new BadRequestException("Profile image must be a valid image URL ending with .png, .svg, .jpg, .jpeg, or .gif");
        }

        boolean bucketExits = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!bucketExits) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String fileName = file.getOriginalFilename();
        fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);
        boardRepository.updateCover(boardId, fileName);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/boards/" + fileName)
                .toUriString();

        return FileMetadata.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
    }

    @SneakyThrows
    @Override
    public InputStream getFileByFileName(UUID boardId, String fileName) {
        getBoardByBoardId(boardId);
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new NotFoundException("File not found: " + fileName);
            }
            throw new RuntimeException("MinIO error", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error accessing MinIO", e);
        }
    }

    @Override
    public void updateIsFavourite(UUID boardId, boolean isFavourite) {
        getBoardByBoardId(boardId);
        boardRepository.updateIsFavourite(boardId, isFavourite);
    }

    @Override
    public void inviteMemberToBoard(UUID boardId, List<String> emails) {
        getBoardByBoardId(boardId);
        emails.forEach(emailService::sendInvitations);
    }

    @Override
    public Board getBoardByIdWithCurrentUserId(UUID boardId) {
        Board board = getBoardByBoardId(boardId);
        if (board == null) {
            throw new NotFoundException("Board id not found");
        }
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return boardRepository.getBoardByIdWithCurrentUserId(boardId,userId);
    }

    @Override
    public HashSet<Board> getAllBoardsForCurrentUser() {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return boardRepository.getAllBoardsForCurrentUser(userId);
    }

    @Override
    public HashSet<Board> getAllBoards() {
        return boardRepository.getAllBoards();
    }

    public void validateCurrentUserRoles(UUID boardId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);
        if (roleName == null || !roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new ForbiddenException("You're not manager in this board can't assign role");
        }
    }


}
