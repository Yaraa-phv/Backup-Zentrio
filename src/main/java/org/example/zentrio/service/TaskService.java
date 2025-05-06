package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTask(UUID boardId, UUID ganttBarId, TaskRequest taskRequest);

    List<Task> getAllTasks(UUID boardId, UUID ganttBarId);

    Task getTaskById( UUID taskId);

    List<Task> getTaskByTitle(UUID boardId,String title);

    Task updateTaskById(UUID taskId, @Valid TaskRequest taskRequest);

    Task updateTaskTitleByTaskId(UUID taskId, String title);

    Task updateTaskDescriptionByTaskId(UUID taskId, String description);

    Task deleteTaskByTaskId( UUID taskId);

    Task assignUserToTaskWithRole(UUID assignedByUserId, UUID assignedToUserId, UUID boardId, UUID taskId);

    Task editRoleNameByBoardIdAndUserId(UUID boardId, String email);

}
