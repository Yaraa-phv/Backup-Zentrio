package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.model.Task;
import org.example.zentrio.repository.TaskRepository;
import org.example.zentrio.service.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final BoardService boardService;
    private final GanttChartService ganttChartService;
    private final GanttBarService ganttBarService;

    public List<UUID> checkExistedBoardIdAndGanttBarId(UUID boardId, UUID ganttBarId){
        if (boardId != null) {
            boardService.checkExistedBoardId(boardId);
        } else {
            throw new NotFoundException("Board Id not found! Cannot create task!");
        }

        GanttChart ganttChartByBoardId = ganttChartService.getGanttChartByBoardId(boardId);
        UUID existedGanttChartId = ganttChartByBoardId.getGanttChartId();

        if (ganttBarId == null){
            throw new NotFoundException("Gantt Bar Id not found!");
        }

        GanttBar ganttBarByGanttChartIdAndGanttBarId = ganttBarService.getGanttBarByGanttChartIdAndGanttBarId(existedGanttChartId, ganttBarId);
        if (ganttBarByGanttChartIdAndGanttBarId == null){
            throw new NotFoundException("Gantt Bar Id cannot find!");
        }
//        UUID existedGanttBarId = ganttBarByGanttChartIdAndGanttBarId.getGanttBarId();
        if (!ganttBarId.equals(ganttBarByGanttChartIdAndGanttBarId.getGanttBarId())){
            throw new NotFoundException("Gantt Bar Id not found!");
        }
        return List.of(boardId, ganttBarByGanttChartIdAndGanttBarId.getGanttBarId());
    }

    @Override
    public Task createTask(UUID boardId, UUID ganttBarId, TaskRequest taskRequest) {

        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);

        return taskRepository.createTask(boardId, ganttBarId, taskRequest);
    }

    @Override
    public List<Task> getAllTasks(UUID boardId, UUID ganttBarId) {

        List<UUID> existedIds = checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
//        UUID existedWorkspaceId = existedIds.get(0);
//        System.out.println("workspace id : " + existedWorkspaceId);
        UUID existedBoardId = existedIds.get(0);
        System.out.println("board id : " + existedBoardId);
        UUID existedGanttBarId = existedIds.get(1);
        System.out.println("gantt bar id : " + existedGanttBarId);

        return taskRepository.getAllTasks(existedBoardId, existedGanttBarId);
    }

    @Override
    public Task getTaskById(UUID boardId, UUID ganttBarId, UUID taskId) {

        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
        if (taskId == null){
            throw new NotFoundException("Task Id not found!");
        } else {
            return taskRepository.getTaskById(boardId, ganttBarId, taskId);
        }
    }

    @Override
    public List<Task> getTaskByTitle(UUID boardId, UUID ganttBarId, String title) {

        checkExistedBoardIdAndGanttBarId(boardId, ganttBarId);
        if (title == null){
            throw new NotFoundException("Task title not found!");
        } else {
         return taskRepository.getTaskByTitle(boardId, ganttBarId, title);
        }
    }

    @Override
    public Task updateTaskById(UUID boardId, UUID ganttBarId, UUID taskId, TaskRequest taskRequest) {

        checkExistedBoardIdAndGanttBarId(boardId, ganttBarId);
        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.updateTaskById(taskId, taskRequest);
        }
    }

    @Override
    public Task updateTaskTitleByTaskId(UUID boardId, UUID ganttBarId, UUID taskId, String title) {

        checkExistedBoardIdAndGanttBarId(boardId, ganttBarId);

        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.updateTaskTitleByTaskId(taskId, title);
        }
    }

    @Override
    public Task updateTaskDescriptionByTaskId(UUID boardId, UUID ganttBarId, UUID taskId, String description) {

        checkExistedBoardIdAndGanttBarId(boardId, ganttBarId);
        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.updateTaskDescriptionByTaskId(taskId, description);
        }
    }

    @Override
    public Task deleteTaskByTaskId(UUID boardId, UUID ganttBarId, UUID taskId) {

        checkExistedBoardIdAndGanttBarId(boardId, ganttBarId);
        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.deleteTaskByTaskId(taskId);
        }
    }
}