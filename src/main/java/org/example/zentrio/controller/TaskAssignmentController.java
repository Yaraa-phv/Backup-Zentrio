package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Task;
import org.example.zentrio.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks/task-assignment")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Task Assignment Controller is not DONE YET")
public class TaskAssignmentController {

    private final TaskService taskService;

    @PostMapping("/assign-role/{board-id}/{user-id}")
    public ResponseEntity<ApiResponse<Task>> assignRole(@PathVariable("board-id") UUID boardId, @PathVariable("user-id") UUID assignToUserId) {
        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Assigned role successfully")
                .payload(taskService.assignRole(boardId,assignToUserId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping("/assign-task-with-role")
    public ResponseEntity<ApiResponse<Task>> assignUserToTaskWithRole(UUID assignedToUserId, UUID taskId) {
        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Assign task and role to user successfully")
                .payload(taskService.assignUserToTaskWithRole(assignedToUserId,taskId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
