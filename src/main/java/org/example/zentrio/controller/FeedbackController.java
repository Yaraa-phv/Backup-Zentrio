package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.FeedbackRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Feedback;
import org.example.zentrio.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/feedback")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Feedback Controller")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/tasks")
    @Operation(summary = "creat feedback ")
    public ResponseEntity<ApiResponse<Feedback>>  createFeedback(
             @Valid @RequestBody FeedbackRequest feedbackRequest) {
        ApiResponse<Feedback> response = ApiResponse.<Feedback>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("Feedback Create Successfully!")
                .payload(feedbackService.createFeedback(feedbackRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.status(201).body(response);
    }

    @GetMapping("/tasks/{task-id}")
    @Operation(summary = "Geat all  feedback by taskId ")
    public ResponseEntity<ApiResponse<Set<Feedback>>>  getAllFeedback(
            @PathVariable("task-id") UUID taskId) {
        ApiResponse<Set<Feedback>> response = ApiResponse.<Set<Feedback>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Feedback Geat Successfully!")
                .payload(feedbackService.getAllFeedback(taskId))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }

    @GetMapping("/{feedback-id}/tasks/{task-id}")
    @Operation(summary = "Get feedback feedback by Id ")
    public ResponseEntity<ApiResponse<Feedback>>  getFeedbackById(
            @PathVariable("feedback-id") UUID feedbackId, @PathVariable("task-id")  UUID taskId) {
        ApiResponse response = ApiResponse.<Feedback>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Feedback Geat Successfully!")
                .payload(feedbackService.getFeedbackById(feedbackId, taskId))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }



    @PutMapping("/{feedback-id}")
    @Operation(summary = "Update feedback by feedback-id ")
    public ResponseEntity<ApiResponse<Feedback>>  UpdateFeedbackById(
            @PathVariable("feedback-id") UUID feedbackId, @Valid @RequestBody FeedbackRequest feedbackRequest) {
        ApiResponse response = ApiResponse.<Feedback>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Feedback Geat Successfully!")
                .payload(feedbackService.UpdateFeedbackById(feedbackId,feedbackRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }

    @DeleteMapping("/{feedback-id}/tasks/{task-id}")
    @Operation(summary = "Delete   feedback by feedback-id ")
    public ResponseEntity<ApiResponse<Void>>  deleteFeedbackById(
            @PathVariable("feedback-id") UUID feedbackId, @PathVariable("task-id") UUID taskId) {
        feedbackService.deleteFeedbackById(feedbackId, taskId);
        ApiResponse response = ApiResponse.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Delete Feedback By Feedback-Id Successfully!")
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }


}
