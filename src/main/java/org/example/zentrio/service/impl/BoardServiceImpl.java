package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.BoardResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.*;
import org.example.zentrio.repository.*;
import org.example.zentrio.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public Board createBoard(BoardRequest boardRequest, UUID workspaceId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId, userId);
        if (workspace == null) {
            throw new NotFoundException("Workspace id " + workspaceId + " not found");
        }
        Board board = boardRepository.createBoard(boardRequest, workspaceId);
        UUID boardId = board.getBoardId();
        UUID roleId = roleRepository.getRoleIdByRoleName(RoleName.ROLE_MANAGER.toString());
        boardRepository.insertMember(userId, boardId, roleId);
        return board;
    }

    @Override
    public ApiResponse<HashSet<Board>> getAllBoardsByWorkspaceId(UUID workspaceId, Integer page, Integer size) {
        Workspace existedWorkspaceIdById = workspaceRepository.getWorkspaceByWorkspaceIdForAllUsers(workspaceId);
        if (existedWorkspaceIdById == null) {
            throw new NotFoundException("Workspace cannot found!");
        }
        Integer offset = (page -1) * size;

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
    public Board deleteBoardByBoardId(UUID boardId) {
        getBoardByBoardId(boardId);
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);

        if (roleName == null) {
            throw new NotFoundException("You're doesn't have a ROLE MANGER to deleted this board");
        }

        if (!roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new NotFoundException("You're doesn't have permission to deleted this board");
        }
        return boardRepository.deleteBoardByBoardId(boardId);
    }

    @Override
    public Board getBoardByBoardId(UUID boardId) {
        Board board = boardRepository.getBoardByBoardId(boardId);
        System.out.println("currentBoardId: " + board);
        if (board == null) {
            throw new NotFoundException("Board id " + boardId + " not found");
        }

        return board;
    }

    @Override
    public List<Board> getBoardByBoardTitle(String boardTitle) {
        List<Board> board = boardRepository.getBoardByTitle(boardTitle);
        if (board == null) {
            throw new NotFoundException("Board title " + boardTitle + " not found");
        }
        return board;
    }


    @Override
    public BoardResponse getBoardByBoardIdWithMember(UUID boardId) {
        getBoardByBoardId(boardId);
        return boardRepository.getBoardByBoardIdWithMember(boardId);
    }

    @Override
    public void assignRoleToBoard(UUID boardId, UUID assigneeId, RoleName roleName) {
        getBoardByBoardId(boardId);
        validateCurrentUserRoles(boardId);

        AppUser userId = appUserRepository.getUserById(assigneeId);
        if(userId == null) {
            throw new NotFoundException("Assignee id " + assigneeId + " not found");
        }

        if(roleName == RoleName.ROLE_MANAGER) {
            throw new BadRequestException("Cannot assign users as a manager");
        }

        UUID existingMemberId  = boardRepository.getMemberIdByUserIdAndBoardId(assigneeId, boardId);

        if (existingMemberId  != null) {
            throw new ConflictException("Member with ID " + assigneeId + "is already assigned");
        }

        UUID roleId = roleRepository.getRoleIdByRoleName(roleName.toString());

        roleRepository.insertToMember(assigneeId,boardId,roleId);
    }

    public void validateCurrentUserRoles(UUID boardId){
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);
        if(roleName == null || !roleName.equals(RoleName.ROLE_MANAGER.toString())){
            throw new ForbiddenException("You're not manager in this board can't assign role");
        }
    }


}
