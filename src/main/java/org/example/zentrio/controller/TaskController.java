package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Task;
import org.example.zentrio.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Task Controller")
public class TaskController {


    private final TaskService taskService;

    @Operation(summary = "Create task by boards ID, and gantt bars ID", description = "Created task with boards ID and gantt bars ID")
    @PostMapping("/boards/{board-id}/gantt-bars/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<Task>> createTaskByBoardIdAndGanttBarId(
            @Valid @RequestBody TaskRequest taskRequest,
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId) {

        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Created task successfully")
                .payload(taskService.createTaskByBoardIdAndGanttBarId(taskRequest, boardId, ganttBarId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @Operation(summary = "Get task by task ID", description = "GEt task by ID with current boards")
    @GetMapping("/{task-id}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(@PathVariable("task-id") UUID taskId) {
        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Get task by ID successfully")
                .payload(taskService.getTaskById(taskId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Get all task by board ID, and gantt bar ID", description = "Get all task by specific board ID with gantt bar ID")
    @GetMapping("/boards/{board-id}/gantt-bars/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<List<Task>>> getTasksByBoardIdAndGanttBarId(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        ApiResponse<List<Task>> apiResponse = taskService.getAllTasksByBoardIdAndGanttBarId(boardId, ganttBarId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Update task by ID", description = "Updated task by ID with boards ID and gantt bars ID")
    @PutMapping("/{task-id}/boards/{board-id}/gantt-bars/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<Task>> updateTaskByIdWithBoardIdAndGanttBarId(
            @Valid @RequestBody TaskRequest taskRequest,
            @PathVariable("task-id") UUID taskId,
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId) {
        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Updated task by ID successfully")
                .payload(taskService.updateTaskByIdWithBoardIdAndGanttBarId(taskRequest, taskId, boardId, ganttBarId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Deleted task by ID", description = "Deleted task by ID with boards ID and gantt bars ID")
    @DeleteMapping("/{task-id}/boards/{board-id}/gantt-bars/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<Task>> deleteTaskByIdWithBoardIdAndGanttBarId(
            @PathVariable("task-id") UUID taskId,
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId) {

        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Deleted task by ID successfully")
                .payload(taskService.deleteTaskByIdWithBoardIdAndGanttBarId(taskId, boardId, ganttBarId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Get task by title", description = "Get task by title with specific boards ID")
    @GetMapping("/{title}/boards/{board-id}")
    public ResponseEntity<ApiResponse<HashSet<Task>>> getTaskByTitleWithBoardId(
            @PathVariable("title") String title,
            @PathVariable("board-id") UUID boardId) {
        ApiResponse<HashSet<Task>> apiResponse = ApiResponse.<HashSet<Task>>builder()
                .success(true)
                .message("Get task by ID successfully")
                .payload(taskService.getTaskByTitleWithBoardId(title, boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Assigned role with task ID", description = "Assigned role leader tasks with specific users")
    @PostMapping("/{task-id}/members/{assignee-id}")
    public ResponseEntity<?> assignLeaderToTask(
            @PathVariable("task-id") UUID taskId,
            @PathVariable("assignee-id") UUID assigneeId) {
        taskService.assignLeaderToTask(taskId, assigneeId);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Assigned role with task ID successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}