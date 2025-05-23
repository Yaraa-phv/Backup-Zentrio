package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.*;
import org.example.zentrio.repository.*;
import org.example.zentrio.service.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final BoardService boardService;
    private final GanttBarService ganttBarService;
    private final RoleRepository roleRepository;
    private final BoardRepository boardRepository;
    private final GanttBarRepository ganttBarRepository;


    private void validateTaskTimeWithGanttBar(TaskRequest taskRequest, GanttBar ganttBar) {
        LocalDateTime now = LocalDateTime.now();

        //  Task start time cannot be in the past
        if (taskRequest.getStartedAt().isBefore(now)) {
            throw new BadRequestException("Task start time cannot be in the past.");
        }

        //  Task finish time must be after start time
        if (taskRequest.getFinishedAt().isBefore(taskRequest.getStartedAt())) {
            throw new BadRequestException("Task finish time must be after start time.");
        }

        //  Task finish time must not exceed GanttBar's finish time
        if (taskRequest.getFinishedAt().isAfter(ganttBar.getFinishedAt())) {
            throw new BadRequestException("Task finish time cannot exceed GanttBar's finish time.");
        }

        //  Task start time must not be before GanttBar's start time
        if (taskRequest.getStartedAt().isBefore(ganttBar.getStartAt())) {
            throw new BadRequestException("Task start time cannot be before GanttBar's start time.");
        }
    }


    private void validateBoardAndGanttBar(UUID boardId, UUID ganttBarId) {
        boardService.getBoardByBoardId(boardId);
        ganttBarService.getGanttBarByGanttBartID(ganttBarId);
    }


    private void validateBoardAndGanttBarAndTaskTime(UUID boardId, UUID ganttBarId, TaskRequest taskRequest) {
        //  Validate if board exists
        boardService.getBoardByBoardId(boardId);

        //  Load GanttBar entity from DB
        GanttBar ganttBar = ganttBarRepository.getGanttBarByGanttBartID(ganttBarId);
        if (ganttBar == null) {
            throw new NotFoundException("Gantt bar with ID " + ganttBarId + " not found");
        }

        //  Validate task timing against GanttBar timing
        validateTaskTimeWithGanttBar(taskRequest, ganttBar);
    }


    private void validateRoleManageTask(UUID boardId, UUID userId) {
        List<String> roles = roleRepository.getRolesNameByBoardIdAndUserId(boardId, userId);

        if (roles == null || roles.isEmpty()) {
            throw new ForbiddenException("You are not a member of this board");
        }

        boolean hasAccess = roles.stream()
                .anyMatch(role -> role.equals(RoleName.ROLE_MANAGER.toString()) || role.equals(RoleName.ROLE_LEADER.toString()));

        if (!hasAccess) {
            throw new ForbiddenException("Only Project Managers or Team Leaders can manage tasks");
        }
    }


    public void validateTaskIdWithBoardIdAndGanttBarId(UUID taskId, UUID boardId, UUID ganttBarId) {
        getTaskById(taskId);
        validateBoardAndGanttBar(boardId, ganttBarId);
    }

    public void validateCurrentUserRoles(UUID boardId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String roleName = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);
        if (roleName == null || !roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new ForbiddenException("You're not manager in this board can't assign role");
        }
    }



    @Override
    public Task createTaskByBoardIdAndGanttBarId(TaskRequest taskRequest, UUID boardId, UUID ganttBarId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();

        // Validate user has permission (Manager or TL)
        validateRoleManageTask(boardId, userId);

        // Determine the role of the user in the board
        String role = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId); // returns "MANAGER" or "TEAM_LEADER"
        System.out.println("role: " + role);

        UUID creatorMemberId;

        if (RoleName.ROLE_MANAGER.toString().equals(role)) {
            creatorMemberId = boardRepository.getManagerMemberIdByUserIdAndBoardId(userId, boardId);
        } else if (RoleName.ROLE_LEADER.toString().equals(role)) {
            creatorMemberId = boardRepository.getTeamLeaderMemberIdByUserIdAndBoardId(userId, boardId);

        } else {
            throw new ForbiddenException("You're not a Manager or Team Leader in this board can't create task");
        }

        // Validate timing and references
        validateBoardAndGanttBarAndTaskTime(boardId, ganttBarId, taskRequest);

        // Create the task with the correct "created_by" (manager or TL)
        Task task = taskRepository.createTaskByBoardIdAndGanttBarId(taskRequest, boardId, ganttBarId, creatorMemberId);
        // the same id
        taskRepository.insertTaskAssignment(task.getTaskId(),creatorMemberId,creatorMemberId);

        return task;
    }


    //  taskRepository.insertTaskAssignment(task.getTaskId(),managerId,memberId);
    @Override
    public Task getTaskById(UUID taskId) {
        Task task = taskRepository.getTaskByTaskId(taskId);
        if (task == null) {
            throw new NotFoundException("Task with ID " + taskId + " not found");
        }
        return task;
    }

    @Override
    public ApiResponse<List<Task>> getAllTasksByBoardIdAndGanttBarId(UUID boardId, UUID ganttBarId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        validateBoardAndGanttBar(boardId, ganttBarId);
        List<Task> taskList = taskRepository.getAllTasksByBoardIdAndGanttBarId(boardId, ganttBarId, size, offset);
        Integer totalElements = taskRepository.countTasksByBoardIdAndGanttBarId(boardId, ganttBarId);

        int totalPages = (int) Math.ceil(totalElements / (double) size);
        return ApiResponse.<List<Task>>builder()
                .success(true)
                .message("Get all tasks successfully")
                .payload(taskList)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .pagination(new Pagination(page, totalElements, totalPages))
                .build();
    }

    @Override
    public Task updateTaskByIdWithBoardIdAndGanttBarId(TaskRequest taskRequest, UUID taskId, UUID boardId, UUID ganttBarId) {
        // Fetch the task to check creator
        Task task = getTaskById(taskId);

        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();

        // Validate user role (Manager or Team Leader)
        validateRoleManageTask(boardId, userId);

        // Get role name for user in board
        String role = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);

        if (RoleName.ROLE_LEADER.toString().equals(role)) {
            // If Team Leader, allow only if creator
            UUID teamLeaderMemberId = boardRepository.getTeamLeaderMemberIdByUserIdAndBoardId(userId, boardId);
            if (!task.getCreatedBy().equals(teamLeaderMemberId)) {
                throw new ForbiddenException("Team Leader can only update tasks they created");
            }
        } else if (!RoleName.ROLE_MANAGER.toString().equals(role)) {
            // If not Manager or Team Leader, forbid update
            throw new ForbiddenException("You are not have permission to update this task");
        }

        validateBoardAndGanttBarAndTaskTime(boardId, ganttBarId, taskRequest);

        // Update task in DB
        return taskRepository.updateTaskByIdWithBoardIdAndGanttBarId(taskRequest, taskId, boardId, ganttBarId);
    }


    @Override
    public Task deleteTaskByIdWithBoardIdAndGanttBarId(UUID taskId, UUID boardId, UUID ganttBarId) {
        Task task = getTaskById(taskId);
        validateTaskIdWithBoardIdAndGanttBarId(taskId, boardId, ganttBarId);
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        validateRoleManageTask(boardId,userId);
        String role = roleRepository.getRoleNameByBoardIdAndUserId(boardId, userId);

        if (RoleName.ROLE_LEADER.toString().equals(role)) {
            UUID teamLeaderMemberId = boardRepository.getTeamLeaderMemberIdByUserIdAndBoardId(userId, boardId);
            if (!task.getCreatedBy().equals(teamLeaderMemberId)) {
                throw new ForbiddenException("Team Leader can only deleted tasks they created");
            }
        } else if (!RoleName.ROLE_MANAGER.toString().equals(role)) {
            throw new ForbiddenException("You are not have permission to deleted this task");
        }

        return taskRepository.deleteTaskByIdWithBoardIdAndGanttBarId(taskId, boardId, ganttBarId);
    }

    @Override
    public HashSet<Task> getTaskByTitleWithBoardId(String title, UUID boardId) {
        boardService.getBoardByBoardId(boardId);
        List<Task> task = taskRepository.getTaskByTitleWithBoardId(title, boardId);
        return new HashSet<>(task);
    }

    @Override
    public void assignLeaderToTask(UUID taskId, UUID assigneeId) {
        Task task = getTaskById(taskId);
        validateCurrentUserRoles(task.getBoardId());
        if (taskRepository.isAlreadyAssigned(taskId)) {
            throw new ConflictException("Task's with ID " + taskId + " is already assigned Leader");
        }

        UUID currentUserId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        UUID assignerId = boardRepository.getMemberIdByUserIdAndBoardId(currentUserId, task.getBoardId());
        System.out.println("assignerId: " + assignerId);


        System.out.println("assigner id: " + assignerId);
        UUID leaderId = taskRepository.findLeaderIdByUserIdAndBoardId(assigneeId, task.getBoardId());
        if (leaderId == null) {
            throw new ForbiddenException("You're not a leader of this board");
        }


        taskRepository.insertTaskAssignment(taskId, assignerId, leaderId);
    }

}