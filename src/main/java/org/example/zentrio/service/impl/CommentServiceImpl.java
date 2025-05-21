package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.model.Comment;
import org.example.zentrio.repository.CommentRepository;
import org.example.zentrio.service.ChecklistService;
import org.example.zentrio.service.CommentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ChecklistService checklistService;

    public UUID userId (){
        AppUser appUser= (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }

    @Override
    public Comment createComment(UUID checklistId, CommentRequest commentRequest) {
//        Checklist checklist= checklistService.getChecklistChecklistId(checklistId);
//        if (checklist == null) {
//            throw new NotFoundException("Checklist not found");
//        }


        return commentRepository.createComment(checklistId,commentRequest);
    }
}
