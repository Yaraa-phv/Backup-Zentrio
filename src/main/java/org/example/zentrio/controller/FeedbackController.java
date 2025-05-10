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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/feedback")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Feedback Controller")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/{task-id}")
    @Operation(summary = "created feedback ")
    public ResponseEntity<ApiResponse<Feedback>>  createFeedback(
            @PathVariable("task-id") UUID taskId,
            @Valid @RequestBody FeedbackRequest feedbackRequest) {
        ApiResponse response = ApiResponse.<Feedback>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("Feedback Create Successfully!")
                .payload(feedbackService.createFeedback(taskId,feedbackRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }

    @GetMapping("/{task-id}")
    @Operation(summary = "Get all feedback by taskId ")
    public ResponseEntity<ApiResponse<HashMap<String,Feedback>>>  getAllFeedback(
            @PathVariable("task-id") UUID taskId) {
        ApiResponse response = ApiResponse.<HashMap<String,Feedback>>builder()
                .success(true)
                .status(HttpStatus.FOUND)
                .message("Getting feedback successfully!")
                .payload(feedbackService.getAllFeedback(taskId))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }

    @GetMapping("/get-feedback-by-id/{feedback-id}")
    @Operation(summary = "Get all  feedback by taskId ")
    public ResponseEntity<ApiResponse<Feedback>>  getFeedbackById(
            @PathVariable("feedback-id") UUID feedbackId) {
        ApiResponse response = ApiResponse.<Feedback>builder()
                .success(true)
                .status(HttpStatus.FOUND)
                .message("Get feedback by ID successfully!")
                .payload(feedbackService.getFeedbackById(feedbackId))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }


    @PutMapping("/{feedback-id}")
    @Operation(summary = "Update   feedback by feedback-id ")
    public ResponseEntity<ApiResponse<Feedback>>  UpdateFeedbackById(
            @PathVariable("feedback-id") UUID feedbackId, @Valid @RequestBody FeedbackRequest feedbackRequest) {
        ApiResponse response = ApiResponse.<Feedback>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("Update feedback successfully!")
                .payload(feedbackService.UpdateFeedbackByid(feedbackId,feedbackRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }

    @DeleteMapping("/{feedback-id}")
    @Operation(summary = "Delete feedback by feedback-id ")
    public ResponseEntity<ApiResponse<Void>>  deleteFeedbackById(
            @PathVariable("feedback-id") UUID feedbackId) {
        feedbackService.deleteFeedbackByid(feedbackId);
        ApiResponse response = ApiResponse.<Void>builder()
                .success(true)
                .status(HttpStatus.ACCEPTED)
                .message("Delete feedback by ID Successfully!")
                .timestamp(LocalDateTime.now())
                .build();
        return   ResponseEntity.ok(response);
    }


}
