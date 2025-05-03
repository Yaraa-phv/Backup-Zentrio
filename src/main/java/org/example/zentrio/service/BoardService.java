package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.model.Board;

import java.util.List;
import java.util.UUID;

public interface BoardService {

    Board createBoard(BoardRequest boardRequest,UUID workspaceId);

    List<Board> getAllBoardsByWorkspaceId(UUID workspaceId);

    Board getBoardByWorkspaceIdAndBoardId(UUID workspaceId, UUID boardId);

    Board updateBoardByBoardId(BoardRequest boardRequest, UUID boardId);

    Board deleteBoardByBoardId(UUID boardId);

    Board getBoardByBoardId(UUID boardId);

    List<Board> getBoardByBoardTitle(String boardTitle);

    Board updateBoardTitleByBoardId(UUID boardId, String boardTitle);
}