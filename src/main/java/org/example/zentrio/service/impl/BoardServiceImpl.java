package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.repository.BoardRepository;
import org.example.zentrio.repository.RoleRepository;
import org.example.zentrio.repository.WorkspaceRepository;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceService workspaceService;
    private final WorkspaceRepository workspaceRepository;
    private final RoleRepository roleRepository;

    @Override
    public UUID checkExistedBoardId(UUID boardId) {

        if (boardId == null){
            throw new NotFoundException("Request Board Id not found!");
        }
        Board boardById = boardRepository.getBoardByBoardId(boardId);
        if (boardById == null){
            throw new NotFoundException("Board Id is not found!");
        }
//        UUID existedBoardId = boardById.getBoardId();
        if (!boardId.equals(boardById.getBoardId())){
            throw new NotFoundException("Board Id can not find!");
        } else {
            return boardById.getBoardId();
        }

    }

    @Override
    public Board createBoard(BoardRequest boardRequest,UUID workspaceId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId,userId);
        if(workspace == null) {
            throw new NotFoundException("Workspace id " + workspaceId + " not found");
        }
        Board board = boardRepository.createBoard(boardRequest,workspaceId);
        UUID boardId = board.getBoardId();
        UUID roleId = roleRepository.getRoleNameByRoleId("ROLE_MANAGER");
        boardRepository.insertMember(userId,boardId,roleId);
        return board;
    }

    @Override
    public List<Board> getAllBoardsByWorkspaceId(UUID workspaceId) {
        UUID existedWorkspaceId = workspaceService.checkExistedWorkspaceId(workspaceId);
        return boardRepository.getAllBoardsByWorkspaceId(existedWorkspaceId);
    }



    @Override
    public Board getBoardByWorkspaceIdAndBoardId(UUID workspaceId, UUID boardId) {
        UUID existedWorkspaceId = workspaceService.checkExistedWorkspaceId(workspaceId);
        return boardRepository.getBoardByWorkspaceIdAndBoardId(existedWorkspaceId, boardId);
    }

    @Override
    public Board updateBoardByBoardId(BoardRequest boardRequest, UUID boardId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByUserIdAndBoardId(boardId,userId);

        if(roleName == null) {
            throw new NotFoundException("You're doesn't have a ROLE MANGER to update this board");
        }

        if(!roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new NotFoundException("You're doesn't have permission to update this board");
        }

        return boardRepository.updateBoardByBoardId(boardRequest,boardId);
    }

    @Override
    public Board deleteBoardByBoardId(UUID boardId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByUserIdAndBoardId(boardId,userId);

        System.out.println("roleName: " + roleName);

        if(roleName == null) {
            throw new NotFoundException("You're doesn't have a ROLE MANGER to deleted this board");
        }

        if(!roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new NotFoundException("You're doesn't have permission to deleted this board");
        }
        return boardRepository.deleteBoardByBoardId(boardId);
    }

    @Override
    public Board getBoardByBoardId(UUID boardId) {
        Board board = boardRepository.getBoardByBoardId(boardId);
        System.out.println("currentBoardId: " + board);
        if(board == null) {
            throw new NotFoundException("Board id " + boardId + " not found");
        }

        return board;
    }

    @Override
    public List<Board> getBoardByBoardTitle(String boardTitle) {
        List<Board> board = boardRepository.getBoardByTitle(boardTitle);
        if(board == null) {
            throw new NotFoundException("Board title " + boardTitle + " not found");
        }
        return board;
    }

    @Override
    public Board updateBoardTitleByBoardId(UUID boardId, String boardTitle) {
        Board currentBoardId = boardRepository.getBoardByBoardId(boardId);
        if(currentBoardId == null) {
            throw new NotFoundException("Board id " + boardId + " not found");
        }

        return boardRepository.updateBoardTitleByBoardId(boardId,boardTitle);
    }

}
