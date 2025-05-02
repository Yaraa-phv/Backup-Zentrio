package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.model.Board;
import org.example.zentrio.repository.BoardRepository;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceService workspaceService;

    @Override
    public Board createBoard(BoardRequest boardRequest) {

        UUID existedWorkspaceId = workspaceService.checkExistedWorkspaceId(boardRequest.getWorkspaceId());
        if (boardRequest.getWorkspaceId() == existedWorkspaceId){
            boardRequest.setIsVerified(true);
        }

        return boardRepository.createBoard(boardRequest);
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
}
