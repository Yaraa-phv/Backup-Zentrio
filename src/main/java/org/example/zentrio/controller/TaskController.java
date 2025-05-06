package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @PostMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<Task>> createTask(@PathVariable("board-id") UUID boardId, @PathVariable("gantt-bar-id") UUID ganttBarId, @RequestBody @Valid TaskRequest taskRequest){
        ApiResponse<Task> response = ApiResponse.<Task> builder()
                .success(true)
                .message("Created board successfully!")
                .status(HttpStatus.CREATED)
                .payload(taskService.createTask(boardId, ganttBarId, taskRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all tasks by board id, and gantt bar id")
    @GetMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}")
    public ResponseEntity<ApiResponse<List<Task>>> getAllTasks(@PathVariable("board-id") UUID boardId, @PathVariable("gantt-bar-id") UUID ganttBarId){
        ApiResponse<List<Task>> response = ApiResponse.<List<Task>> builder()
                .success(true)
                .message("Created tasks successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.getAllTasks(boardId, ganttBarId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get task by board id, gantt bar id, and task id")
    @GetMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}/task-id/{task-id}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(@PathVariable("board-id") UUID boardId, @PathVariable("gantt-bar-id") UUID ganttBarId, @PathVariable("task-id") UUID taskId){
        ApiResponse<Task> response = ApiResponse.<Task> builder()
                .success(true)
                .message("Get task by id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.getTaskById( boardId, ganttBarId, taskId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get task by board id, gantt bar id, and task title")
    @GetMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}/task-title/{title}")
    public ResponseEntity<ApiResponse<List<Task>>> getTaskByTitle(@PathVariable("board-id") UUID boardId, @PathVariable("gantt-bar-id") UUID ganttBarId, @PathVariable("title") String title){
        ApiResponse<List<Task>> response = ApiResponse.<List<Task>> builder()
                .success(true)
                .message("Get workspace by title successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.getTaskByTitle(boardId, ganttBarId, title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit task by board id, gantt bar id, and task id")
    @PutMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}/update/{task-id}")
    public ResponseEntity<ApiResponse<Task>> updateTaskById(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @PathVariable("task-id") UUID taskId,
            @RequestBody @Valid TaskRequest taskRequest){
        ApiResponse<Task> response = ApiResponse.<Task> builder()
                .success(true)
                .message("Update task by id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.updateTaskById(boardId, ganttBarId, taskId, taskRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit task title by board id, gantt bar id, and task id")
    @PatchMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}/updateTitle/{task-id}")
    public ResponseEntity<ApiResponse<Task>> updateTaskTitleByTaskId(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @PathVariable("task-id") UUID taskId,
            @RequestBody String title){
        ApiResponse<Task> response = ApiResponse.<Task> builder()
                .success(true)
                .message("Update task title by task id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.updateTaskTitleByTaskId(boardId, ganttBarId, taskId, title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit task description by board id, gantt bar id, and task id")
    @PatchMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}/updateDescription/{task-id}")
    public ResponseEntity<ApiResponse<Task>> updateTaskDescriptionByTaskId(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @PathVariable("task-id") UUID taskId,
            @RequestBody String description){
        ApiResponse<Task> response = ApiResponse.<Task> builder()
                .success(true)
                .message("Update task description by task id successfully!")
                .status(HttpStatus.OK)
                .payload(taskService.updateTaskDescriptionByTaskId(boardId, ganttBarId, taskId, description))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete task by board id, gantt bar id, and task id")
    @DeleteMapping("/board-id/{board-id}/gantt-bar-id/{gantt-bar-id}/delete/{task-id}")
    public ResponseEntity<DeleteApiResponse<Task>> deleteTaskByTaskId(
            @PathVariable("board-id") UUID boardId,
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @PathVariable("task-id") UUID taskId){

        taskService.deleteTaskByTaskId(boardId, ganttBarId, taskId);

        DeleteApiResponse<Task> response = DeleteApiResponse.<Task>builder()
                .success(true)
                .message("Delete task by task id successfully!")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }


    //From Fanau
    @PostMapping("/assign-task-with-role")
    public ResponseEntity<ApiResponse<Task>> assignUserToTaskWithRole(UUID assignedByUserId, UUID assignedToUserId, UUID boardId, UUID taskId) {
        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Assign task and role to user successfully")
                .payload(taskService.assignUserToTaskWithRole(assignedByUserId,assignedToUserId,boardId,taskId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    //From Fanau
    @Operation(summary = "Edit role by board id and email")
    @PutMapping("/edit-role-name/board-id/{board-id}/email/{email}")
    public ResponseEntity<ApiResponse<Task>> editRoleNameByBoardIdAndUserId(@PathVariable("board-id") UUID boardId, @PathVariable("email") String email ){
        ApiResponse<Task> apiResponse = ApiResponse.<Task>builder()
                .success(true)
                .message("Edit role by board id and email successfully")
                .payload(taskService.editRoleNameByBoardIdAndUserId(boardId, email))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}