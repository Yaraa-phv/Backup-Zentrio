package org.example.zentrio.service;

import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.model.Board;

import java.util.List;
import java.util.UUID;

public interface BoardService {

    Board createBoard(BoardRequest boardRequest);

    List<Board> getAllBoardsByWorkspaceId(UUID workspaceId);

    Board getBoardByWorkspaceIdAndBoardId(UUID workspaceId, UUID boardId);
}