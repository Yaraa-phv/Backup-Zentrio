package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.*;
import org.example.zentrio.repository.*;
import org.example.zentrio.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    private final BoardRepository boardRepository;
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

    public UUID checkExistedBoardByUserId(UUID currentUserId, UUID boardId){

        if (boardId != null) {
            boardService.checkExistedBoardId(boardId);
        } else {
            throw new NotFoundException("Board Id not found! Cannot create task!");
        }

        Member checkMember = memberRepository.getMemberByUserIdAndBoardId(currentUserId, boardId);
        System.out.println("checkMember : " + checkMember);
        System.out.println("checkMember id : " + checkMember.getMemberId());
        if (checkMember == null){
            throw new BadRequestException("You are not a member here!");
        }

        UUID roleIdOfExistedMember = memberRepository.getRoleIdByMemberId(checkMember.getMemberId());
        System.out.println("roleIdOfExistedMember : " + roleIdOfExistedMember);
        String roleOfExistedMember = roleRepository.getRoleNameByRoleId(roleIdOfExistedMember);
        System.out.println("roleOfExistedMember : " + roleOfExistedMember);
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

    public UUID checkExistedTaskByTaskId(UUID taskId){
        Task task = taskRepository.getTaskByTaskId(taskId);
        if (task == null){
            throw new NotFoundException("Task Id not found!");
        }
        taskId = task.getTaskId();

        checkExistedBoardIdAndGanttBarId( task.getBoardId(), task.getGanttBarId());
        UUID currentUserId = authService.getCurrentAppUserId();
        UUID boardId = checkExistedBoardByUserId(currentUserId, task.getBoardId());
        Task existedTask = taskRepository.getTaskById(boardId, task.getGanttBarId(), taskId);
        if (!taskId.equals(existedTask.getTaskId())){
            throw new NotFoundException("Task Id not found!");
        }
        return taskId;
    }


    @Override
    public Task createTask(UUID boardId, UUID ganttBarId, TaskRequest taskRequest) {
        checkExistedBoardIdAndGanttBarId( boardId, ganttBarId);
        UUID currentUserId = authService.getCurrentAppUserId();
        boardId = checkExistedBoardByUserId(currentUserId, boardId);
        return taskRepository.createTask(boardId, ganttBarId, taskRequest);
    }

    @Override
    public ApiResponse<HashMap<String, Task>> getAllTasks(UUID boardId, UUID ganttBarId, Integer page, Integer size) {


        Integer offset = page * size;

        List<UUID> existedIds = checkExistedBoardIdAndGanttBarId(boardId, ganttBarId);
        UUID existedBoardId = existedIds.get(0);
        System.out.println("board id : " + existedBoardId);
        UUID existedGanttBarId = existedIds.get(1);
        System.out.println("gantt bar id : " + existedGanttBarId);

        List<Task> taskList = taskRepository.getAllTasks(boardId, ganttBarId, size, offset);


        HashMap<String, Task> tasks = new HashMap<>();
        for (Task task : taskList) {
            tasks.put(task.getTitle(), task);
        }

        Integer totalElements = taskRepository.countTasksByBoardIdAndGanttBarId(boardId, ganttBarId);
        Integer totalPages = (int) Math.ceil(totalElements / (double) size);

        return ApiResponse.<HashMap<String, Task>>builder()
                .success(true)
                .message("Get all tasks successfully")
                .payload(tasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .pagination(new Pagination(page, totalElements, totalPages))
                .build();
    }

    @Override
    public Task getTaskById( UUID taskId) {
        Task task = taskRepository.getTaskByTaskId(taskId);

        checkExistedBoardIdAndGanttBarId( task.getBoardId(), task.getGanttBarId());
        if (taskId == null){
            throw new NotFoundException("Task Id not found!");
        } else {
            return taskRepository.getTaskById(task.getBoardId(), task.getGanttBarId(), taskId);
        }
    }

    @Override
    public HashMap<String, Task> getTaskByTitle(UUID boardId, String title) {

        if (boardId != null) {
            boardService.checkExistedBoardId(boardId);
        } else {
            throw new NotFoundException("Board Id not found! Cannot create task!");
        }

        HashMap<String, Task> tasks = new HashMap<>();
        for (Task t : taskRepository.getAllTaskByBoardIdAndTitle(boardId, title)){
         tasks.put(t.getTitle(), t);
        }

        if (title == null){
            throw new NotFoundException("Task title not found!");
        }
        return tasks;
    }

    @Override
    public Task updateTaskById(UUID taskId, TaskRequest taskRequest) {

        checkExistedTaskByTaskId(taskId);
        return taskRepository.updateTaskById(taskId, taskRequest);
    }

    @Override
    public Task updateTaskTitleByTaskId( UUID taskId, String title) {

        checkExistedTaskByTaskId(taskId);
        return taskRepository.updateTaskTitleByTaskId(taskId, title);

    }

    @Override
    public Task updateTaskDescriptionByTaskId(UUID taskId, String description) {

        checkExistedTaskByTaskId(taskId);
        return taskRepository.updateTaskDescriptionByTaskId(taskId, description);

    }

    @Override
    public Task deleteTaskByTaskId( UUID taskId) {

        checkExistedTaskByTaskId(taskId);
        return taskRepository.deleteTaskByTaskId(taskId);

    }

    //From Fanau
    @Override
    public Task assignRole(UUID boardId, UUID assignToUserId) {
        Board boardByBoardId = boardRepository.getBoardByBoardId(boardId);


        if (boardByBoardId == null){
            throw new NotFoundException("Board cannot found by board id!");
        }
        UUID getBoardId = boardByBoardId.getBoardId();
        if (!boardId.equals(getBoardId)){
            throw new BadRequestException("Board id is not existed!");
        }

        boolean isAlreadyAssignedRole = boardRepository.isMemberAlreadyAssignedRoleToBoard(boardId, assignToUserId);
        if(isAlreadyAssignedRole) {
            throw new BadRequestException("You are already member of this board");
        }

        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        System.out.println("userid : " + userId);
        String roleName = roleRepository.getRoleNameByUserIdAndBoardId(boardId, userId);
        if (roleName == null) {
            throw new BadRequestException("You are doesn't have a role to assign this task");
        }

        if (!roleName.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new BadRequestException("You are not PM can't assign role for member in this task");
        }

//        load roleId by passing roleName;
        UUID roleId = roleRepository.getRoleIdByRoleName(RoleName.ROLE_LEADER.toString());
//        System.out.println("targetBoardId = " + getBoardId);
//        input role for member that has invite
//        roleRepository.insertUserRoles(userIdFromEmailInput, roleId);

//        insert userId that have roleId = ROLE_LEADER into boardId
        roleRepository.insertToMember(assignToUserId, getBoardId, roleId);
        return null;
    }


    @Override
    public Task assignUserToTaskWithRole(UUID assignedToUserId, UUID taskId) {

        System.out.println("assignedToUserId = " + assignedToUserId);

        Task task = taskRepository.getTaskByTaskId(taskId);
        UUID boardId = task.getBoardId();

        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();

        // load user id input for sure id of the user are correct in current board
        UUID assignUserIdInMemberTable = roleRepository.getMemberIdByUserIdAndBoardId(boardId, userId); // pm id assign

        String managerRole = roleRepository.getRoleNameByUserIdAndBoardId(boardId, userId);
//        System.out.println("managerRole = " + managerRole);

        if (managerRole == null) {
            throw new BadRequestException("You are doesn't have a role to assign this task");
        }
        if (!managerRole.equals(RoleName.ROLE_MANAGER.toString())) {
            throw new BadRequestException("You are not PM can't assign member in this task");
        }


        System.out.println("assignUserIdInMemberTable = " + assignUserIdInMemberTable);
        if (assignUserIdInMemberTable == null) {
            throw new BadRequestException("Assigned user does not exist");
        }

        // load role of the user that are assign in this task doesn't have role leader -> exist
        UUID assignTo = roleRepository.getMemberIdByUserIdAndBoardId(boardId, assignedToUserId); // assign to
        System.out.println("assignTo = " + assignTo);

        if(assignTo == null) {
            throw new BadRequestException("User id that u was assign was " + assignedToUserId + " not exist");
        }

        // load role for user using id of user input
        String roleName = roleRepository.getRoleNameByUserIdAndBoardId(boardId, assignedToUserId);
        System.out.println("roleName = " + roleName);


        if (roleName == null) {
            throw new BadRequestException("You doesn't have role as a leader you should ");
        }
        // if user doesn't have any role in board with table member
        if (!roleName.equals(RoleName.ROLE_LEADER.toString())) {
            throw new BadRequestException("You should let's manager assign role first ");
        }

        boolean alreadyAssigned = taskRepository.isMemberAlreadyAssignedToTask(taskId, assignTo);
        if(alreadyAssigned) {
            throw new BadRequestException("This team leader is already assigned to this task.");
        }


        // insert assignerId , assignToId , and taskId , with role as a Leader
        taskRepository.assignMemberToTaskWithRole(assignUserIdInMemberTable, assignTo, taskId);
        return task;
    }


}