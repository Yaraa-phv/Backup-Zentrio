package org.example.zentrio.service;

import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.BoardResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.model.Board;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface BoardService {

    Board createBoard(BoardRequest boardRequest,UUID workspaceId);

    ApiResponse<HashSet<Board>> getAllBoardsByWorkspaceId(UUID workspaceId, Integer page, Integer size);

    Board updateBoardByBoardId(BoardRequest boardRequest, UUID boardId);

    Board deleteBoardByBoardId(UUID boardId);

    Board getBoardByBoardId(UUID boardId);

    List<Board> getBoardByBoardTitle(String boardTitle);


    BoardResponse getBoardByBoardIdWithMember(UUID boardId);

    void assignRoleToBoard(UUID boardId, UUID assigneeId, RoleName roleName);
}