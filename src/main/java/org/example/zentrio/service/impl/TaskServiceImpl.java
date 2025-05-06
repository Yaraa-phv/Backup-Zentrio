package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.model.Member;
import org.example.zentrio.model.Task;
import org.example.zentrio.repository.AppUserRepository;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final AppUserRepository appUserRepository;
    private final AuthService authService;

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

    public UUID checkExistedBoardByUserId(UUID curretUserId, UUID boardId){

        if (boardId != null) {
            boardService.checkExistedBoardId(boardId);
        } else {
            throw new NotFoundException("Board Id not found! Cannot create task!");
        }

        Member checkMember = memberRepository.getMemberByUserIdAndBoardId(curretUserId, boardId);
        if (checkMember == null){
            throw new BadRequestException("You are not a member here!");
        }

        UUID roleIdOfExistedMember = memberRepository.getRoleIdByMemberId(checkMember.getRoleId());
        String roleOfExistedMember = roleRepository.getRoleNameByRoleId(roleIdOfExistedMember);
        if (roleOfExistedMember.isEmpty()){
            throw new BadRequestException("You are not a member here!");
        }
        if (!roleOfExistedMember.equals(RoleName.ROLE_MANAGER.toString())){
            throw new NotFoundException("You are not a MANAGER here!");
        }
        if (roleOfExistedMember.equals(RoleName.ROLE_MANAGER.toString())){
            return boardId;
        }

        return boardId;
    }


    @Override
    public Task createTask(UUID boardId, UUID ganttBarId, TaskRequest taskRequest) {

        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
        UUID currentUserId = authService.getCurrentAppUserId();
        boardId = checkExistedBoardByUserId(currentUserId, boardId);

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

        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
        UUID currentUserId = authService.getCurrentAppUserId();
        boardId = checkExistedBoardByUserId(currentUserId, boardId);
        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.updateTaskById(taskId, taskRequest);
        }
    }

    @Override
    public Task updateTaskTitleByTaskId(UUID boardId, UUID ganttBarId, UUID taskId, String title) {

        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
        UUID currentUserId = authService.getCurrentAppUserId();
        boardId = checkExistedBoardByUserId(currentUserId, boardId);
        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.updateTaskTitleByTaskId(taskId, title);
        }
    }

    @Override
    public Task updateTaskDescriptionByTaskId(UUID boardId, UUID ganttBarId, UUID taskId, String description) {

        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
        UUID currentUserId = authService.getCurrentAppUserId();
        boardId = checkExistedBoardByUserId(currentUserId, boardId);
        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.updateTaskDescriptionByTaskId(taskId, description);
        }
    }

    @Override
    public Task deleteTaskByTaskId(UUID boardId, UUID ganttBarId, UUID taskId) {

        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
        UUID currentUserId = authService.getCurrentAppUserId();
        boardId = checkExistedBoardByUserId(currentUserId, boardId);
        Task existedTask = taskRepository.getTaskById(boardId, ganttBarId, taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }else {
            return taskRepository.deleteTaskByTaskId(taskId);
        }
    }

    //From Fanau
    @Override
    public Task assignUserToTaskWithRole(UUID assignedByUserId, UUID assignedToUserId, UUID boardId, UUID taskId) {

        // load user id input for sure id of the user are correct in current board
        UUID assignUserId = roleRepository.getMemberIdByUserIdAndBoardId(boardId, assignedByUserId);
        if (assignUserId == null) {
            throw new BadRequestException("Assigned user does not exist");
        }

        // load role of the user that are assign in this task doesn't have role leader -> exist
        UUID assignTo = roleRepository.getMemberIdByUserIdAndBoardId(boardId, assignedToUserId);

        // load role for user using id of user input
        String roleName = roleRepository.getRoleNameByUserIdAndBoardId(boardId, assignedToUserId);
        if (roleName == null) {
            throw new BadRequestException("You do not have a role to assign this task");
        }
        // if user doesn't have any role in board with table member
        if (!roleName.equals(RoleName.ROLE_LEADER.toString())) {
            throw new BadRequestException("You should let's manager assign role first ");
        }

        // insert assignerId , assignToId , and taskId , with role as a Leader
        taskRepository.assignMemberToTaskWithRole(assignedByUserId, assignTo, taskId);
        return null;
    }

    @Override
    public Task editRoleNameByBoardIdAndUserId(UUID boardId, String email) {


//        load user by email input
        UUID userIdFromEmailInput = appUserRepository.getUserIdByEmail(email);

        System.out.println("userIdFromEmailInput " + userIdFromEmailInput);
//
        if (userIdFromEmailInput == null) {
            throw new BadRequestException("Email not found " + email + "please try again");
        }

//        boolean isAlreadyHasRole = roleRepository.existsByBoardIdAndUserId(boardId, userIdFromEmailInput);
//        if (isAlreadyHasRole) {
//            throw new BadRequestException("You already have a role to assign this task");
//        }
//
////        load roleId by passing roleName;
//        UUID roleId = roleRepository.getRoleIdByRoleName("ROLE_LEADER");
//        UUID targetBoardId = boardRepository.existsByBoardId(boardId);
//        System.out.println("targetBoardId = " + targetBoardId);
////        input role for member that has invite
////        roleRepository.insertUserRoles(userIdFromEmailInput, roleId);
//
////        insert userId that have roleId = ROLE_LEADER into boardId
//        roleRepository.insertToMember(userIdFromEmailInput, targetBoardId, roleId);
        return null;
    }

}