//package org.example.zentrio.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.example.zentrio.dto.request.ChecklistRequest;
//import org.example.zentrio.enums.RoleName;
//import org.example.zentrio.exception.BadRequestException;
//import org.example.zentrio.exception.NotFoundException;
//import org.example.zentrio.model.Board;
//import org.example.zentrio.model.Checklist;
//import org.example.zentrio.model.Role;
//import org.example.zentrio.model.Task;
//import org.example.zentrio.repository.*;
//import org.example.zentrio.service.AuthService;
//import org.example.zentrio.service.ChecklistService;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class ChecklistServiceImpl implements ChecklistService {
//
//    private final ChecklistRepository checklistRepository;
//    private final TaskRepository taskRepository;
//    private final MemberRepository memberRepository;
//    private final AuthService authService;
//    private final RoleRepository roleRepository;
//    private final BoardRepository boardRepository;
//
//    @Override
//    public UUID checkExistedTaskId(UUID taskId, UUID currentUserId){
//
//        Task taskById = taskRepository.getTaskByTaskId(taskId);
//        if (taskById == null){
//            throw new BadRequestException("Cannot find task by id!");
//        }
//        taskId = taskById.getTaskId();
//
//        Board getBoardByTaskId = boardRepository.getBoardByTaskId(taskId);
//        if (getBoardByTaskId == null){
//            throw new NotFoundException("Cannot found board by task id!");
//        }
//
//        UUID existedMemberId = memberRepository.getMemberIdByUserIdAndBoardId(currentUserId, getBoardByTaskId.getBoardId());
//        if (existedMemberId == null){
//            throw new NotFoundException("User is not a member in this board!");
//        }
//        UUID roleIdOfExistedMember = memberRepository.getRoleIdByMemberId(existedMemberId);
//        String roleOfExistedMember = roleRepository.getRoleNameByRoleId(roleIdOfExistedMember);
//        if (roleOfExistedMember.isEmpty()){
//            throw new NotFoundException("You are not a LEADER here!");
//        }
//        if (!roleOfExistedMember.equals(RoleName.ROLE_LEADER.toString())){
//            throw new NotFoundException("You are not a LEADER here!");
//        }
//        if (roleOfExistedMember.equals(RoleName.ROLE_LEADER.toString())){
//            return taskId;
//        }
//
//        return taskId;
//    }
//
//    @Override
//    public UUID checkTaskIdToGetChecklist(UUID taskId){
//
//        Task taskById = taskRepository.getTaskByTaskId(taskId);
//        if (taskById == null){
//            throw new BadRequestException("Cannot find task by id!");
//        }
//        taskId = taskById.getTaskId();
//
//        Board getBoardByTaskId = boardRepository.getBoardByTaskId(taskId);
//        if (getBoardByTaskId == null){
//            throw new NotFoundException("Cannot found board by task id!");
//        }
//        return taskId;
//
//    }
//
//    public String checkRoleUserById(UUID currentUserId){
//
//        UUID existedMemberId = memberRepository.getRoleIdByUserIdAsAMember(currentUserId);
//        UUID roleIdOfExistedMember = memberRepository.getRoleIdByMemberId(existedMemberId);
//
////        currentUserId = existedMember.getUserId();
//
//        return roleRepository.getRoleNameByRoleId(roleIdOfExistedMember);
//    }
//
//    @Override
//    public Checklist createChecklist(UUID taskId, ChecklistRequest checklistRequest) {
//
//        UUID currentUserId = authService.getCurrentAppUserId();
//        System.out.println("Current user : " + currentUserId);
//        taskId = checkExistedTaskId(taskId, currentUserId);
//
//        System.out.println("checklist title : " + checklistRequest.getTitle());
//
//
//        return checklistRepository.createChecklist(taskId, checklistRequest);
//    }
//
//    @Override
//    public HashMap<String, Checklist> getAllChecklistByTaskId(UUID taskId) {
//
//        HashMap<String , Checklist> checklists = new HashMap<>();
//        taskId = checkTaskIdToGetChecklist(taskId);
//
//        for (Checklist c : checklistRepository.getAllChecklistByTaskId(taskId)){
//            checklists.put(c.getChecklistId().toString(), c);
//            System.out.println(c);
//        }
//
//        return checklists;
//
//    }
//
//    @Override
//    public Checklist getChecklistByTaskIdAndChecklistId(UUID taskId, UUID checklistId) {
//
//        taskId = checkTaskIdToGetChecklist(taskId);
//        if (taskId == null){
//            throw new NotFoundException("Checklist not found by task id!");
//        }
//
//        Checklist checklist = checklistRepository.getChecklistByTaskIdAndChecklistId(taskId, checklistId);
//        if (checklist == null){
//            throw new NotFoundException("Checklist not found by task id and checklist id!!");
//        }
//
//        return checklist;
//
//    }
//
//    @Override
//    public HashMap<String, Checklist> getChecklistByTaskIdAndTitle(UUID taskId, String title) {
//
//        taskId = checkTaskIdToGetChecklist(taskId);
//        taskId = checkTaskIdToGetChecklist(taskId);
//        if (taskId == null){
//            throw new NotFoundException("Checklist not found by task id!");
//        }
//        HashMap<String, Checklist> checklists = new HashMap<>();
//        for (Checklist c : checklistRepository.getChecklistByTaskIdAndTitle(taskId, title)){
//            checklists.put(c.getChecklistId().toString(), c);
//        }
//
//        return checklists;
//
//    }
//
//    @Override
//    public Checklist updateChecklistById(UUID taskId, UUID checklistId, ChecklistRequest checklistRequest) {
//
//        UUID currentUserId = authService.getCurrentAppUserId();
//        taskId = checkExistedTaskId(taskId, currentUserId);
//        taskId = checkTaskIdToGetChecklist(taskId);
//        if (taskId == null){
//            throw new NotFoundException("Checklist not found by task id!");
//        }
//        Checklist checklist = getChecklistByTaskIdAndChecklistId(taskId, checklistId);
//
//        return checklistRepository.updateChecklistById(checklist.getTaskId(), checklist.getChecklistId(), checklistRequest);
//    }
//
//    @Override
//    public Checklist deleteChecklistByTaskIdAndChecklist(UUID taskId, UUID checklistId) {
//
//        UUID currentUserId = authService.getCurrentAppUserId();
//        taskId = checkExistedTaskId(taskId, currentUserId);
//        taskId = checkTaskIdToGetChecklist(taskId);
//        if (taskId == null){
//            throw new NotFoundException("Checklist not found by task id!");
//        }
//        Checklist checklist = getChecklistByTaskIdAndChecklistId(taskId, checklistId);
//        if (checklist == null){
//            throw new NotFoundException("Checklist not found by task id and checklist id!");
//        }
//
//        return checklistRepository.deleteChecklistByTaskIdAndChecklist(checklist.getTaskId(), checklist.getChecklistId());
//    }
//
//    @Override
//    public Checklist assignMemberToChecklist(UUID assignedByUserId, UUID assignToUserId, UUID checklistId, UUID taskId) {
//<<<<<<< HEAD
////        // Get the member ID of the user assigning (must be a leader of the task)
////        UUID assignerMemberId = taskRepository.findMemberIdByUserIdAndTaskId(taskId, assignedByUserId);
////
////        // Verify that the assigner has the ROLE_LEADER for this task
////        String assignerRole = taskRepository.getRoleNameByUserIdAndTaskId(taskId, assignerMemberId);
////        if (!assignerRole.contains(RoleName.ROLE_LEADER.toString())) {
////            throw new BadRequestException("Only a leader can assign members to a checklist.");
////        }
////
////        // Get the member ID of the user to be assigned
////        UUID assigneeMemberId = memberRepository.getMemberIdByUserId(assignToUserId);
////        if(assigneeMemberId == null){
////            throw new NotFoundException("Assign user with id " + assignToUserId + " not found!");
////        }
////
////        boolean isAlreadyAssigned = checklistRepository.isExistByUserIdAndTaskId(checklistId,assigneeMemberId);
////        if (isAlreadyAssigned){
////            throw new BadRequestException("Checklist with member id " + assigneeMemberId + " already assigned!");
////        }
////
////        // Perform the assignment
////        memberRepository.insertIntoChecklistWithRoleMember(checklistId, assignerMemberId, assigneeMemberId);
////
////        // Optionally return something meaningful, currently returns null
//=======
//        // Get the member ID of the user assigning (must be a leader of the task)
//        UUID assignerMemberId = taskRepository.findMemberIdByUserIdAndTaskId(taskId, assignedByUserId);
//
//        // Verify that the assigner has the ROLE_LEADER for this task
//        String assignerRole = taskRepository.getRoleNameByUserIdAndTaskId(taskId, assignerMemberId);
//        if (assignerRole == null){
//            throw new NotFoundException("Assigner member not found!");
//        }
//        if (!assignerRole.equals(RoleName.ROLE_LEADER.name())) {
//            throw new BadRequestException("Only a leader can assign members to a checklist.");
//        }
//
//        // Get the member ID of the user to be assigned
//        UUID assigneeMemberId = memberRepository.getMemberIdByUserId(assignToUserId);
//        if(assigneeMemberId == null){
//            throw new NotFoundException("Assign user with id " + assignToUserId + " not found!");
//        }
//
//        boolean isAlreadyAssigned = checklistRepository.isExistByUserIdAndTaskId(checklistId,assigneeMemberId);
//        if (isAlreadyAssigned){
//            throw new BadRequestException("Checklist with member id " + assigneeMemberId + " already assigned!");
//        }
//
//        // Perform the assignment
//        memberRepository.insertIntoChecklistWithRoleMember(checklistId, assignerMemberId, assigneeMemberId);
//
//        // Optionally return something meaningful, currently returns null
//>>>>>>> 31096f2d2c7ac1ba0d1eb2ab1843ab4b9bfdd634
//        return null;
//    }
//
//}