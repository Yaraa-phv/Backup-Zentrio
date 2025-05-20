package org.example.zentrio.controller;

import com.google.api.services.drive.model.File;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Comment;
import org.example.zentrio.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/comment")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Comment Controller")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("creat-comment/{checklistId}")
    @Operation(summary = "Create Comment By Checklist ID")
    public ResponseEntity<ApiResponse<Comment>> createComment
            (@PathVariable("checklistId") UUID checklistId, @Valid @RequestBody CommentRequest commentRequest)
    {
        ApiResponse<Comment>  response= ApiResponse.<Comment>builder()
                .success(true)
                .message("Create Comment by CheckListId Successfully")
                .payload(commentService.createComment(checklistId,commentRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("get-comment/{commentId}")
    @Operation(summary = "Get Comment By Comment ID")
    public ResponseEntity<ApiResponse<Comment>> getCommentByCommentId
            (@PathVariable("commentId") UUID commentId)
    {
        ApiResponse<Comment>  response= ApiResponse.<Comment>builder()
                .success(true)
                .message("Get Comment by Comment Successfully")
                .payload(commentService.getCommentByCommentId(commentId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("get-all-comments/{checkListId}")
    @Operation(summary = "Get All Comments")
    public ResponseEntity<ApiResponse<Set<Comment>>> getAllComments(@PathVariable("checkListId") UUID checkListId)
    {
        ApiResponse<Set<Comment>>  response= ApiResponse.<Set<Comment>>builder()
                .success(true)
                .message("Get All Comments Successfully")
                .payload(commentService.getAllComments(checkListId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("delete-comment-byId/{commentId}")
    @Operation(summary = "Delete  Comment by CommentId")
    public ResponseEntity<ApiResponse<Comment>> deleteCommentByCommentId(@PathVariable("commentId") UUID commentId)
    {
        ApiResponse<Comment>  response= ApiResponse.<Comment>builder()
                .success(true)
                .message("Delete  Comment by Comment-Id Successfully")
                .payload(commentService.deleteCommentByCommentId(commentId))
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("update-comment-byId/{commentId}")
    @Operation(summary = "Update  Comment by CommentId")
    public ResponseEntity<ApiResponse<Comment>> UpdateCommentByCommentId(
            @PathVariable("commentId") UUID commentId, @Valid @RequestBody CommentRequest commentRequest)
    {
        ApiResponse<Comment>  response= ApiResponse.<Comment>builder()
                .success(true)
                .message("Update Comment by Comment-Id Successfully")
                .payload(commentService.UpdateCommentByCommentId(commentId,commentRequest))
                .status(HttpStatus.ACCEPTED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }




}
