package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AssignedRoleRequest;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.MemberResponse;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.FileMetadata;
import org.example.zentrio.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

    @Operation(summary = "Created new board by workspace ID", description = "create a new board by specific workspace ID")
    @PostMapping("/workspaces/{workspace-id}")
    public ResponseEntity<ApiResponse<Board>> createBoard(@Valid @RequestBody BoardRequest boardRequest) {
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Create board successfully")
                .payload(boardService.createBoard(boardRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Get all boards by workspace ID", description = "Get all boards by specific workspace ID")
    @GetMapping("workspaces/{workspace-id}")
    public ResponseEntity<ApiResponse<HashSet<Board>>> getAllBoardsByWorkspaceId(
            @PathVariable("workspace-id") UUID workspaceId,
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        ApiResponse<HashSet<Board>> response = boardService.getAllBoardsByWorkspaceId(workspaceId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Update board by board ID And Workspace ID", description = "Update board by board ID And specific workspace ID")
    @PutMapping("/{board-id}/workspaces/{workspace-id}")
    public ResponseEntity<ApiResponse<Board>> updateBoardByBoardId(
            @Valid @RequestBody BoardRequest boardRequest,
            @PathVariable("board-id") UUID boardId
    ) {
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Update board successfully")
                .payload(boardService.updateBoardByBoardId(boardRequest, boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Deleted board by ID", description = "Deleted a board by board ID")
    @DeleteMapping("/{board-id}/workspaces/{workspace-id}")
    public ResponseEntity<?> deleteBoardByBoardId(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("workspace-id") UUID workspaceId) {
        boardService.deleteBoardByBoardId(boardId,workspaceId);
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Delete board successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Get board by ID ", description = "Get a board by board ID")
    @GetMapping("/{board-id}")
    public ResponseEntity<ApiResponse<Board>> getBoardByBoardId(@PathVariable("board-id") UUID boardId) {
        ApiResponse<Board> apiResponse = ApiResponse.<Board>builder()
                .success(true)
                .message("Get board by ID successfully")
                .payload(boardService.getBoardByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get board by title ", description = "Get a board by board title")
    @GetMapping("/title/{board-title}")
    public ResponseEntity<ApiResponse<HashSet<Board>>> getBoardByBoardTitle(@PathVariable("board-title") String boardTitle) {
        ApiResponse<HashSet<Board>> apiResponse = ApiResponse.<HashSet<Board>>builder()
                .success(true)
                .message("Get board by title successfully")
                .payload(boardService.getBoardByBoardTitle(boardTitle))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Upload cover by board ID", description = "Upload board cover by specific board ID")
    @PostMapping(value = "/{board-id}/cover/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetadata>> uploadCoverByBoardId(
            @PathVariable("board-id") UUID boardId,
            @RequestParam("file") MultipartFile file) {
        FileMetadata fileMetadata = boardService.uploadBoardImage(boardId, file);
        ApiResponse<FileMetadata> apiResponse = ApiResponse.<FileMetadata>builder()
                .success(true)
                .message("Board cover uploaded successfully")
                .payload(fileMetadata)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @Operation(summary = "Get cover board by board ID", description = "Get cover of boards by specific board ID")
    @GetMapping("/{board-id}/cover/{file-name}")
    public ResponseEntity<?> getCoverByBoardIdAndFileName(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("file-name") String fileName
    ) throws IOException {
        InputStream inputStream = boardService.getFileByFileName(boardId, fileName);
        byte[] fileContent = inputStream.readAllBytes();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(fileContent);
    }

    @Operation(summary = "Get board with members", description = "Get board with member information")
    @GetMapping("/{board-id}/members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getBoardWithMembers(@PathVariable("board-id") UUID boardId) {
        ApiResponse<List<MemberResponse>> apiResponse = ApiResponse.<List<MemberResponse>>builder()
                .success(true)
                .message("Get board by title successfully")
                .payload(boardService.getBoardByBoardIdWithMember(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Assigned users role to board", description = "Assigned user role to the specific boards")
    @PostMapping("/{board-id}/users/{assignee-id}/workspaces/{workspace-id}")
    public ResponseEntity<?> assignRoleToBoard(@RequestBody AssignedRoleRequest assignedRoleRequest) {
        boardService.assignRoleToBoard(assignedRoleRequest);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Assigned role to board successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Updated favourite of board by ID", description = "Updated favourite of board by specific board ID")
    @PutMapping("/{board-id}/favourite")
    public ResponseEntity<?> updateBoardFavourite(
            @PathVariable("board-id") UUID boardId,
            @RequestParam boolean isFavourite) {
        boardService.updateIsFavourite(boardId,isFavourite);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Updated favourite of board successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Invite users to board", description = "Invite any users to specific board")
    @PostMapping("/{board-id}/users/invite")
    public ResponseEntity<?> inviteMemberToBoard(
            @PathVariable("board-id") UUID boardId,
            @RequestBody List<String> emails) {
        boardService.inviteMemberToBoard(boardId,emails);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Invited users to board successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
