package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.DeleteApiResponse;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.service.ChecklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checklists")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Checklist Controller")
public class ChecklistController {

    private final ChecklistService checklistService;

    @Operation(summary = "Create checklist by task id")
    @PostMapping("/task-id/{task-id}")
    public ResponseEntity<ApiResponse<Checklist>> createChecklist(
            @PathVariable("task-id") UUID taskId,
            @RequestBody ChecklistRequest checklistRequest){

        ApiResponse<Checklist> response = ApiResponse.<Checklist> builder()
                .success(true)
                .message("Create checklist by task id successfully!")
                .payload(checklistService.createChecklist(taskId, checklistRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get all checklists by task id")
    @GetMapping("/task-id/{task-id}")
    public ResponseEntity<ApiResponse<HashMap<String, Checklist>>> getAllChecklistByTaskId(
            @PathVariable("task-id") UUID taskId){

        ApiResponse<HashMap<String, Checklist>> response = ApiResponse.<HashMap<String, Checklist>> builder()
                .success(true)
                .message("Get all checklists by task id successfully!")
                .payload(checklistService.getAllChecklistByTaskId(taskId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get checklist by task id and checklist id")
    @GetMapping("/task-id/{task-id}/checklist-id/{checklist-id}")
    public ResponseEntity<ApiResponse<Checklist>> getChecklistByTaskIdAndChecklistId(
            @PathVariable("task-id") UUID taskId,
            @PathVariable("checklist-id") UUID checklistId){

        ApiResponse<Checklist> response = ApiResponse.<Checklist> builder()
                .success(true)
                .message("Get checklist by task id and checklist id successfully!")
                .payload(checklistService.getChecklistByTaskIdAndChecklistId(taskId, checklistId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get checklist by task id and title")
    @GetMapping("/task-id/{task-id}/title/{title}")
    public ResponseEntity<ApiResponse<HashMap<String, Checklist>>> getChecklistByTaskIdAndTitle(
            @PathVariable("task-id") UUID taskId,
            @PathVariable("title") String title
    ){
        ApiResponse<HashMap<String, Checklist>> response = ApiResponse.<HashMap<String, Checklist>> builder()
                .success(true)
                .message("Get workspace by title successfully!")
                .status(HttpStatus.OK)
                .payload(checklistService.getChecklistByTaskIdAndTitle(taskId, title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit checklist by task id and checklist id")
    @PutMapping("/task-id/{task-id}/checklist-id/{checklist-id}")
    public ResponseEntity<ApiResponse<Checklist>> updateChecklistById(
            @PathVariable("task-id") UUID taskId,
            @PathVariable("checklist-id") UUID checklistId,
            @RequestBody ChecklistRequest checklistRequest){
        ApiResponse<Checklist> response = ApiResponse.<Checklist> builder()
                .success(true)
                .message("Update task by id successfully!")
                .status(HttpStatus.OK)
                .payload(checklistService.updateChecklistById(taskId, checklistId, checklistRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete checklist by task id and checklist id")
    @DeleteMapping("/task-id/{task-id}/checklist-id/{checklist-id}")
    public ResponseEntity<DeleteApiResponse<Checklist>> deleteChecklistByTaskIdAndChecklist(
            @PathVariable("task-id") UUID taskId,
            @PathVariable("checklist-id") UUID checklistId){

        checklistService.deleteChecklistByTaskIdAndChecklist(taskId, checklistId);

        DeleteApiResponse<Checklist> response = DeleteApiResponse.<Checklist>builder()
                .success(true)
                .message("Delete checklist by task id and checklist id successfully!")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Assign members to checklists",description = "Assign members to any checklist")
    @PostMapping("assign-member-checklist")
    public ResponseEntity<ApiResponse<Checklist>> assignMemberToChecklist(UUID assignedByUserId,UUID assignToUserId,UUID checklistId,UUID taskId) {
        ApiResponse<Checklist> apiResponse = ApiResponse.<Checklist>builder()
                .success(true)
                .message("Assign member to task successfully")
                .payload(checklistService.assignMemberToChecklist(assignedByUserId,assignToUserId,checklistId,taskId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

}