package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.DeleteApiResponse;
import org.example.zentrio.model.Task;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
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

    @Operation(summary = "Create task by board id, and gantt bar id")
    @PostMapping("boards/{board-id}/gantt-bars/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<Task>> createTask(@PathVariable("board-id") UUID boardId, @PathVariable("gantt-bar-id") UUID ganttBarId, @RequestBody @Valid TaskRequest taskRequest) {
        ApiResponse<Task> response = ApiResponse.<Task>builder()
                .success(true)
                .message("Created board successfully!")
                .status(HttpStatus.CREATED)
                .payload(taskService.createTask(boardId, ganttBarId, taskRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all tasks by board id, and gantt bar id")
    @GetMapping("boards/{board-id}/gantt-bars/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<HashSet<Task>>> getAllTasks(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        ApiResponse<HashSet<Task>> response = taskService.getAllTasks(boardId,ganttBarId,page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get task by task id")
    @GetMapping("/{task-id}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(@PathVariable("task-id") UUID taskId) {
        ApiResponse<Task> response = ApiResponse.<Task>builder()
                .success(true)
                .message("Get task by id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.getTaskById(taskId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get task by task title")
    @GetMapping("boards/{board-id}/tasks/{title}")
    public ResponseEntity<ApiResponse<HashMap<String, Task>>> getTaskByTitle(@PathVariable("board-id") UUID boardId, @PathVariable("title") String title) {
        ApiResponse<HashMap<String, Task>> response = ApiResponse.<HashMap<String, Task>>builder()
                .success(true)
                .message("Get workspace by title successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.getTaskByTitle(boardId, title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit task by task id")
    @PutMapping("/{task-id}")
    public ResponseEntity<ApiResponse<Task>> updateTaskById(
            @PathVariable("task-id") UUID taskId,
            @RequestBody @Valid TaskRequest taskRequest) {
        ApiResponse<Task> response = ApiResponse.<Task>builder()
                .success(true)
                .message("Update task by id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.updateTaskById(taskId, taskRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit task title by task id")
    @PatchMapping("/title/{task-id}")
    public ResponseEntity<ApiResponse<Task>> updateTaskTitleByTaskId(
            @PathVariable("task-id") UUID taskId,
            @RequestBody String title) {
        ApiResponse<Task> response = ApiResponse.<Task>builder()
                .success(true)
                .message("Update task title by task id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.updateTaskTitleByTaskId(taskId, title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit task description by task id")
    @PatchMapping("/{task-id}")
    public ResponseEntity<ApiResponse<Task>> updateTaskDescriptionByTaskId(
            @PathVariable("task-id") UUID taskId,
            @RequestBody String description) {
        ApiResponse<Task> response = ApiResponse.<Task>builder()
                .success(true)
                .message("Update task description by task id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.updateTaskDescriptionByTaskId(taskId, description))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete task by task id")
    @DeleteMapping("/{task-id}")
    public ResponseEntity<DeleteApiResponse<Task>> deleteTaskByTaskId(
            @PathVariable("task-id") UUID taskId) {

        taskService.deleteTaskByTaskId(taskId);

        DeleteApiResponse<Task> response = DeleteApiResponse.<Task>builder()
                .success(true)
                .message("Delete task by task id successfully!")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }


    //From Fanau


}