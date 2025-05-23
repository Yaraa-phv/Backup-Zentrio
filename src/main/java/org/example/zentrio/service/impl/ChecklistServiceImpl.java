package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.ForbiddenException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.*;
import org.example.zentrio.repository.*;
import org.example.zentrio.service.ChecklistService;
import org.example.zentrio.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final BoardRepository boardRepository;


    private void validateChecklistWithTaskTime(ChecklistRequest checklistRequest, Task task) {
        LocalDateTime now = LocalDateTime.now();

        //  Checklist start time cannot be in the past
        if (checklistRequest.getStartedAt().isBefore(now)) {
            throw new BadRequestException("Checklist start time cannot be in the past.");
        }

        // Checklist finish time must be the same or after start time
        if (checklistRequest.getFinishedAt().isBefore(checklistRequest.getStartedAt())) {
            throw new BadRequestException("Checklist finish time cannot be before start time.");
        }

        // Checklist finish time must not exceed Task's finish time
        if (checklistRequest.getFinishedAt().isAfter(task.getFinishedAt())) {
            throw new BadRequestException("Checklist finish time cannot exceed Task's finish time.");
        }

        // Checklist start time must not be before Task's start time
        if (checklistRequest.getStartedAt().isBefore(task.getStartedAt())) {
            throw new BadRequestException("Checklist start time cannot be before Task's start time.");
        }
    }

    void validateChecklistIdAndTaskId(UUID checklistId, UUID taskId) {
        Checklist checklist = getChecklistChecklistId(checklistId);
        if (checklist == null) {
            throw new NotFoundException("Checklist with id " + checklistId + " not found.");
        }
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            throw new NotFoundException("Task with ID " + taskId + " not found.");
        }
    }

    private void validateChecklistAccess(UUID taskId, UUID boardId, UUID userId) {
        // Get roles again to check if Leader specific rules apply
        List<String> roles = roleRepository.getRolesNameByBoardIdAndUserId(boardId, userId);
        if (roles.contains(RoleName.ROLE_LEADER.toString())) {
            UUID leaderMemberId = boardRepository.getTeamLeaderMemberIdByUserIdAndBoardId(userId, boardId);
            Task task = taskService.getTaskById(taskId);
            if (task == null) {
                throw new NotFoundException("Task with ID " + taskId + " not found.");
            }
            if (!task.getCreatedBy().equals(leaderMemberId)) {
                throw new ForbiddenException("Team Leader can only manage checklists for tasks they created.");
            }
        }
        // Managers have full access, no further check needed
    }



    @Override
    public Checklist createChecklist(ChecklistRequest checklistRequest, UUID taskId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Task task = taskService.getTaskById(taskId);
        validateChecklistWithTaskTime(checklistRequest, task);
        validateChecklistAccess(taskId,task.getBoardId(),userId);
        UUID memberId = boardRepository.getMemberIdByUserIdAndBoardId(userId, task.getBoardId());
        if(memberId == null){
            throw new ForbiddenException("You are not a member of this board");
        }
        return checklistRepository.createChecklist(checklistRequest, taskId,memberId);
    }

    @Override
    public Checklist getChecklistChecklistId(UUID checklistId) {
        Checklist checklist = checklistRepository.getChecklistById(checklistId);
        if (checklist == null) {
            throw new BadRequestException("Checklist with ID " + checklistId + " not found!");
        }
        return checklist;
    }

    @Override
    public ApiResponse<HashSet<Checklist>> getAllChecklistsByTaskId(UUID taskId, Integer page, Integer size) {
        taskService.getTaskById(taskId);
        int offset = (page - 1) * size;
        Integer totalElement = checklistRepository.countAllChecklistByTaskId(taskId);
        HashSet<Checklist> checklistList = checklistRepository.getAllChecklistsByTaskId(taskId, size, offset);
        int totalPages = (int) Math.ceil(totalElement / (double) size);

        return ApiResponse.<HashSet<Checklist>>builder()
                .success(true)
                .message("Get all checklists by task ID successfully")
                .payload(checklistList)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .pagination(new Pagination(offset, size, totalPages))
                .build();
    }

    @Override
    public Checklist updateChecklistByIdAndTaskId(ChecklistRequest checklistRequest, UUID checklistId, UUID taskId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Task task = taskService.getTaskById(taskId);
        validateChecklistIdAndTaskId(checklistId, taskId);
        validateChecklistWithTaskTime(checklistRequest,task);
        validateChecklistAccess(taskId,task.getBoardId(), userId);
        return checklistRepository.updateChecklistByIdAndTaskId(checklistRequest,checklistId,taskId);
    }

    @Override
    public Checklist deleteChecklistByIdAndTaskId(UUID checklistId, UUID taskId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Task task = taskService.getTaskById(taskId);
        validateChecklistAccess(taskId,task.getBoardId(), userId);
        validateChecklistIdAndTaskId(checklistId, taskId);
        return checklistRepository.deleteChecklistByIdAndTaskId(checklistId,taskId);
    }

    @Override
    public void assignMemberToChecklist(UUID checklistId, UUID taskId, UUID assignedBy, UUID assignedTo) {
        validateChecklistIdAndTaskId(checklistId, taskId);
        Task task = taskService.getTaskById(taskId);

        System.out.println("assignBy " + assignedBy);

        // can replace taskRepository to memberRepository
        UUID assignerId = taskRepository.findMemberIdByUserIdAndTaskId(assignedBy,taskId);
        System.out.println("assignerId: " + assignerId);

        String roleName = roleRepository.getRoleLeaderNameByBoardIdAndUserId(task.getBoardId(),assignedBy);
        if(roleName == null || !roleName.equals(RoleName.ROLE_LEADER.toString())){
            throw new ForbiddenException("User with id " + assignedTo + " are not the leader of this task can't be assigned to this checklist");
        }

        // can replace checklistRepository to memberRepository
        UUID assigneeId = checklistRepository.findMemberIdByBoardIdAndUserId(task.getBoardId(),assignedTo);
        System.out.println("assigneeId: " + assigneeId);
        if(assigneeId == null){
            throw new NotFoundException("You are not a member of this board can't be assigned to this checklist");
        }

        if(checklistRepository.checklistIsAssigned(checklistId,assigneeId)){
            throw new BadRequestException("Member with ID " + assigneeId + " is already assigned to this checklist");
        }
        checklistRepository.insertToChecklistAssignment(checklistId,assignerId,assigneeId);
    }


}