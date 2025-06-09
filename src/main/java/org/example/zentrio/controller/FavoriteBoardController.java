package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.FavoriteBoard;
import org.example.zentrio.service.FavoriteBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/favourites")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Favourites Boards Controller")
public class FavoriteBoardController {

    private final FavoriteBoardService favoriteBoardService;

    @Operation(summary = "Create favorite boards for users by boards ID and users ID",description = "Created favorite boards for users")
    @PostMapping("/boards/{board-id}/users/{user-id}")
    public ResponseEntity<ApiResponse<FavoriteBoard>> createFavouriteBoard(@PathVariable("board-id")UUID boardId, @PathVariable("user-id") UUID userId) {
        ApiResponse<FavoriteBoard> apiResponse = ApiResponse.<FavoriteBoard>builder()
                .success(true)
                .message("Mark favourite board successfully")
                .payload(favoriteBoardService.createFavoriteBoard(boardId, userId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Delete favorite boards for users by boards ID and users ID",description = "Deleted favorite boards for users by board ID and users ID")
    @DeleteMapping("/boards/{board-id}/users/{user-id}")
    public ResponseEntity<?> deleteFavoriteBoard(@PathVariable("board-id")UUID boardId, @PathVariable("user-id")UUID userId) {
        favoriteBoardService.deleteFavoriteBoard(boardId,userId);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Mark favourite board successfully deleted")
                .payload(null)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get all favorite boards by users ID",description = "Get all favorite boards by users ID")
    @GetMapping("favorite/users/{user-id}")
    public ResponseEntity<ApiResponse<HashSet<Board>>> getFavoriteBoards(@PathVariable("user-id")UUID userId) {
        ApiResponse<HashSet<Board>> apiResponse = ApiResponse.<HashSet<Board>>builder()
                .success(true)
                .message("Mark favourite board successfully deleted")
                .payload(favoriteBoardService.getFavoriteBoards(userId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}
