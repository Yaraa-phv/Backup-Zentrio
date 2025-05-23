package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.BoardResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.model.Board;
import org.example.zentrio.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/boards")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Board Controller")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "Created new board", description = "create a new board by workspace ID")
    @PostMapping
    public ResponseEntity<ApiResponse<Board>> createBoard(@Valid @RequestBody BoardRequest boardRequest, UUID workspaceId) {
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Create board successfully.")
                .payload(boardService.createBoard(boardRequest, workspaceId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Get all boards", description = "Get all boards by workspace ID")
    @GetMapping("workspaces/{workspace-id}")
    public ResponseEntity<ApiResponse<HashSet<Board>>> getAllBoardsByWorkspaceId(
            @PathVariable("workspace-id") UUID workspaceId,
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        ApiResponse<HashSet<Board>> response = boardService.getAllBoardsByWorkspaceId(workspaceId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Update board", description = "Update board by board ID")
    @PutMapping("/{board-id}")
    public ResponseEntity<ApiResponse<Board>> updateBoardByBoardId(@Valid @RequestBody BoardRequest boardRequest, @PathVariable("board-id") UUID boardId) {
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Update board successfully.")
                .payload(boardService.updateBoardByBoardId(boardRequest, boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Deleted board", description = "Deleted a board by board ID")
    @DeleteMapping("/{board-id}")
    public ResponseEntity<ApiResponse<Board>> deleteBoardByBoardId(@PathVariable("board-id") UUID boardId) {
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Delete board successfully.")
                .payload(boardService.deleteBoardByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Get board by ID ", description = "Get a board by board ID")
    @GetMapping("/by-id/{board-id}")
    public ResponseEntity<ApiResponse<Board>> getBoardByBoardId(@PathVariable("board-id") UUID boardId) {
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Get board by ID successfully.")
                .payload(boardService.getBoardByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get board by title ", description = "Get a board by board title")
    @GetMapping("/by-title/{board-title}")
    public ResponseEntity<ApiResponse<List<Board>>> getBoardByBoardTitle(@PathVariable("board-title") String boardTitle) {
        ApiResponse<List<Board>> apiResponse = ApiResponse.<List<Board>>builder()
                .success(true)
                .message("Get board by title successfully.")
                .payload(boardService.getBoardByBoardTitle(boardTitle))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get board with members", description = "Get board with member information")
    @GetMapping("members/{board-id}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardWithMembers(@PathVariable("board-id") UUID boardId) {
        ApiResponse<BoardResponse> apiResponse = ApiResponse.<BoardResponse>builder()
                .success(true)
                .message("Get board by title successfully.")
                .payload(boardService.getBoardByBoardIdWithMember(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Assigned role to board", description = "Assigned user role to the specific boards")
    @PostMapping("/boards/{board-id}/users/{assignee-id}")
    public ResponseEntity<?> assignRoleToBoard(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("assignee-id") UUID assigneeId,
            @RequestParam RoleName roleName) {
        boardService.assignRoleToBoard(boardId, assigneeId, roleName);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Assigned role to board successfully.")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
