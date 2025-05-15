package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.model.Comment;
import org.example.zentrio.model.Task;
import org.example.zentrio.repository.ChecklistRepository;
import org.example.zentrio.repository.CommentRepository;
import org.example.zentrio.repository.TaskRepository;
import org.example.zentrio.service.CommentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ChecklistRepository checklistRepository;
    private final TaskRepository taskRepository;


    public UUID userId (){
        AppUser appUser= (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }

    @Override
    public Comment createComment(UUID checklistId, CommentRequest commentRequest) {
        Checklist checklist= checklistRepository.getChecklistByChecklistId(checklistId);
        if (checklist == null) {
            throw new NotFoundException("Checklist not found");
        }
        Task task= taskRepository.getTaskByTaskId(checklist.getTaskId());

        UUID userInCheckList= taskRepository.findMemberIdByUserIdAndTaskId(checklist.getTaskId(), userId());

        String role= taskRepository.getRoleNameByUserIdAndTaskId(checklist.getTaskId(), userInCheckList);
        System.out.println("Role: "+role);
        if (role== null){
            String checkListRole= checklistRepository.getRoleMemberInChecklist(task.getBoardId(), userId());
            UUID memberId = checklistRepository.getIdMemberInChecklist(task.getBoardId(), userId());
            System.out.println("CheckListRole: "+checkListRole);
            if (checkListRole.equals(RoleName.ROLE_MEMBER.name())) {
              Comment  comment = commentRepository.createComment(checklistId,commentRequest, LocalDateTime.now(),memberId);
              return comment;
            }
        }
        if (role.equals(RoleName.ROLE_LEADER.name() )){
            Comment comment= commentRepository.createCommentByleader(checklistId,commentRequest, LocalDateTime.now(),userId());
            return comment;
        }
       return null;

    }
}
