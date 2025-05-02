package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Board;
import org.example.zentrio.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Board Controller")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "Create board by workspaceId")
    @PostMapping()
    public ResponseEntity<ApiResponse<Board>> createBoard(@RequestBody @Valid BoardRequest boardRequest){
        ApiResponse<Board> response = ApiResponse.<Board> builder()
                .success(true)
                .message("Created board successfully!")
                .status(HttpStatus.CREATED)
                .payload(boardService.createBoard(boardRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all boards by workspaceId")
    @GetMapping("/{workspace-id}")
    public ResponseEntity<ApiResponse<List<Board>>> getAllBoardsByWorkspaceId(@PathVariable("workspace-id") UUID workspaceId){
        ApiResponse<List<Board>> response = ApiResponse.<List<Board>> builder()
                .success(true)
                .message("Created workspace successfully!")
                .status(HttpStatus.OK)
                .payload(boardService.getAllBoardsByWorkspaceId(workspaceId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get board by workspace id and board id")
    @GetMapping("{workspace-id}/board-id/{board-id}")
    public ResponseEntity<ApiResponse<Board>> getBoardByWorkspaceIdAndBoardId(@PathVariable("workspace-id") UUID workspaceId, @PathVariable("board-id") UUID boardId){
        ApiResponse<Board> response = ApiResponse.<Board> builder()
                .success(true)
                .message("Get workspace by id successfully!")
                .status(HttpStatus.OK)
                .payload(boardService.getBoardByWorkspaceIdAndBoardId(workspaceId, boardId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}