package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTask(UUID boardId, UUID ganttBarId, TaskRequest taskRequest);

    List<Task> getAllTasks(UUID boardId, UUID ganttBarId);

    Task getTaskById(UUID boardId, UUID ganttBarId, UUID taskId);

    List<Task> getTaskByTitle(UUID boardId, UUID ganttBarId, String title);

    Task updateTaskById(UUID boardId, UUID ganttBarId, UUID taskId, @Valid TaskRequest taskRequest);

    Task updateTaskTitleByTaskId(UUID boardId, UUID ganttBarId, UUID taskId, String title);

    Task updateTaskDescriptionByTaskId(UUID boardId, UUID ganttBarId, UUID taskId, String description);

    Task deleteTaskByTaskId(UUID boardId, UUID ganttBarId, UUID taskId);

}
