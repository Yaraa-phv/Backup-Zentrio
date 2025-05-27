package org.example.zentrio.service;


import jakarta.validation.Valid;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.enums.Stage;
import org.example.zentrio.model.Task;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTaskByBoardIdAndGanttBarId(TaskRequest taskRequest);

    Task getTaskById(UUID taskId);

    ApiResponse<List<Task>> getAllTasksByBoardIdAndGanttBarId(UUID boardId, UUID ganttBarId, Integer page, Integer size);

    Task updateTaskByIdWithBoardIdAndGanttBarId(TaskRequest taskRequest, UUID taskId);

    Task deleteTaskByIdWithBoardIdAndGanttBarId(UUID taskId, UUID boardId, UUID ganttBarId);

    HashSet<Task> getTaskByTitleWithBoardId(String title, UUID boardId);

    void assignLeaderToTask(UUID taskId, UUID assigneeId);

    void moveTask(UUID taskId, Stage stage);

    void updateStatusOfTaskById(UUID taskId, boolean isDone);

    void updateProgressOfTaskById(UUID taskId);


//    Task createTask(UUID boardId, UUID ganttBarId, TaskRequest taskRequest);
//
//    ApiResponse<HashSet<Task>> getAllTasks(UUID boardId, UUID ganttBarId, Integer page, Integer size);
//
//    Task getTaskById(UUID taskId);
//
//    HashMap<String, Task> getTaskByTitle(UUID boardId,String title);
//
//    Task updateTaskById(UUID taskId, TaskRequest taskRequest);
//
//    Task deleteTaskByTaskId( UUID taskId);
//
////    UUID checkExistedBoardByUserId(UUID currentUserId, UUID boardId);
//
//    Task assignRole(UUID boardId,UUID assignToUserId);
//
//    Task assignUserToTaskWithRole(UUID assignedToUserId, UUID taskId);


}
