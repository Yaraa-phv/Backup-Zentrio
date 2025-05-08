package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTask(UUID boardId, UUID ganttBarId, TaskRequest taskRequest);

    HashMap<String, Task> getAllTasks(UUID boardId, UUID ganttBarId);

    Task getTaskById( UUID taskId);

    HashMap<String, Task> getTaskByTitle(UUID boardId,String title);

    Task updateTaskById(UUID taskId, @Valid TaskRequest taskRequest);

    Task updateTaskTitleByTaskId(UUID taskId, String title);

    Task updateTaskDescriptionByTaskId(UUID taskId, String description);

    Task deleteTaskByTaskId( UUID taskId);

    UUID checkExistedBoardByUserId(UUID currentUserId, UUID boardId);

    Task assignRole(UUID boardId,UUID assignToUserId);

    Task assignUserToTaskWithRole(UUID assignedToUserId, UUID taskId);


}
