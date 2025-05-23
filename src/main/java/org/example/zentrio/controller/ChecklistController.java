package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.service.ChecklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checklists")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Checklist Controller")
public class ChecklistController {

    private final ChecklistService checklistService;

    @Operation(summary = "Create checklist by tasks ID", description = "Created checklist with specific tasks ID")
    @PostMapping("/tasks/{task-id}")
    public ResponseEntity<ApiResponse<Checklist>> createChecklist(
            @Valid @RequestBody ChecklistRequest checklistRequest,
            @PathVariable("task-id") UUID taskId) {
        ApiResponse<Checklist> apiResponse = ApiResponse.<Checklist>builder()
                .success(true)
                .message("Created checklist by task ID successfully")
                .payload(checklistService.createChecklist(checklistRequest, taskId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @Operation(summary = "Get checklist by ID", description = "Get checklist by ID with current tasks")
    @GetMapping("/{checklist-id}")
    public ResponseEntity<ApiResponse<Checklist>> getChecklistById(@PathVariable("checklist-id") UUID checklistId) {
        ApiResponse<Checklist> apiResponse = ApiResponse.<Checklist>builder()
                .success(true)
                .message("Get checklist by ID successfully")
                .payload(checklistService.getChecklistById(checklistId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get all checklist by tasks ID", description = "Get all checklist by specific tasks ID")
    @GetMapping("/tasks/{task-id}")
    public ResponseEntity<ApiResponse<HashSet<Checklist>>> getAllChecklistsByTaskId(
            @PathVariable("task-id") UUID taskId,
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        ApiResponse<HashSet<Checklist>> apiResponse = checklistService.getAllChecklistsByTaskId(taskId, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Update checklist by ID and task ID ", description = "Updated checklist by ID with specific tasks ID")
    @PutMapping("/{checklist-id}/tasks/{task-id}")
    public ResponseEntity<ApiResponse<Checklist>> updateChecklistByIdAndTaskId(
            @Valid @RequestBody ChecklistRequest checklistRequest,
            @PathVariable("checklist-id") UUID checklistId,
            @PathVariable("task-id") UUID taskId) {
        ApiResponse<Checklist> apiResponse = ApiResponse.<Checklist>builder()
                .success(true)
                .message("Updated checklist successfully")
                .payload(checklistService.updateChecklistByIdAndTaskId(checklistRequest, checklistId, taskId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Delete checklist by ID and task ID", description = "Deleted checklist by ID with specific tasks ID")
    @DeleteMapping("/{checklist-id}/tasks/{task-id}")
    public ResponseEntity<ApiResponse<Checklist>> deleteChecklistByIdAndTaskId(
            @PathVariable("checklist-id") UUID checklistId,
            @PathVariable("task-id") UUID taskId) {
        ApiResponse<Checklist> apiResponse = ApiResponse.<Checklist>builder()
                .success(true)
                .message("Deleted checklist successfully")
                .payload(checklistService.deleteChecklistByIdAndTaskId(checklistId, taskId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Assigned member to checklist", description = "Assigned member to checklist with specific members ID")
    @PostMapping("{checklist-id}/tasks/{task-id}/members{assigned-by}/members/{assigned-to}/")
    public ResponseEntity<?> assignMemberToChecklist(
            @PathVariable("checklist-id") UUID checklistId,
            @PathVariable("task-id") UUID taskId,
            @PathVariable("assigned-by") UUID assignedBy,
            @PathVariable("assigned-to") UUID assignedTo) {
        checklistService.assignMemberToChecklist(checklistId, taskId, assignedBy, assignedTo);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Assigned member to checklist successfully")
                .payload(null)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}





