package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.FeedbackRequest;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.Feedback;
import org.example.zentrio.model.Task;
import org.example.zentrio.repository.*;
import org.example.zentrio.service.FeedbackService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final BoardRepository boardRepository;

    public UUID userID (){
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUser.getUserId();
    }

    @Override
    public Feedback createFeedback( UUID taskId, FeedbackRequest feedbackRequest) {

        Task task= taskRepository.getTaskByTaskId(taskId);
        System.out.println(task);
        if (task==null) {
            throw new NotFoundException("Task not found");
        }
        String role= roleRepository.getRoleNameByUserIdAndBoardId(task.getBoardId(),userID());
        System.out.println(role);
        if (role == null){
            throw new BadRequestException("You don't have any role here...");
        }
        System.out.println("role:"+role);
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("only ROLE_MANAGER allowed...");
        }
        Feedback feedback = feedbackRepository.createFeedback(LocalDateTime.now(),userID(),taskId,feedbackRequest);
        return feedback;
    }

    @Override
    public HashMap<String,Feedback> getAllFeedback(UUID taskId) {
        HashMap<String,Feedback> feedback = new HashMap<>();
        Task task= taskRepository.getTaskByTaskId(taskId);
        if (task==null) {
            throw new NotFoundException("Task not found");
        }
        for (Feedback f : feedbackRepository.getAllFeedback(taskId)){
            feedback.put(f.getFeedbakId().toString(), f);
        }
        return feedback;
    }

    @Override
    public Feedback UpdateFeedbackByid(UUID feedbackId, FeedbackRequest feedbackRequest) {
        Feedback feedback = feedbackRepository.getFeedbackById(feedbackId);
        if (feedback==null) {
            throw new NotFoundException("Feedback not found");
        }
        Task  task= taskRepository.getTaskByTaskId(feedback.getTaskId());
        if (task==null) {
            throw new NotFoundException("Task not found");
        }
        String role =roleRepository.getRoleNameByUserIdAndBoardId(task.getBoardId(),userID());
        if (role == null){
            throw new BadRequestException("You don't have any role here...");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("only ROLE_MANAGER allowed...");
        }

        return feedbackRepository.UpdateFeedbackByid(feedbackId, feedbackRequest);
    }



    @Override
    public Feedback getFeedbackById(UUID feedbackId) {
        Feedback feedback = feedbackRepository.getFeedbackById(feedbackId);
        if (feedback==null) {
            throw new NotFoundException("Feedback not found");
        }
        return feedback;

    }




    @Override
    public void deleteFeedbackByid(UUID feedbackId) {

        Feedback feedback = feedbackRepository.getFeedbackById(feedbackId);
        if (feedback==null) {
            throw new NotFoundException("Feedback not found");
        }
        Task  task= taskRepository.getTaskByTaskId(feedback.getTaskId());
        if (task==null) {
            throw new NotFoundException("Task not found");
        }
        String role =roleRepository.getRoleNameByUserIdAndBoardId(task.getBoardId(),userID());
        if (role == null){
            throw new BadRequestException("You don't have any role here...");
        }
        if (!role.equals(RoleName.ROLE_MANAGER.name())) {
            throw new BadRequestException("only ROLE_MANAGER allowed...");
        }
        feedbackRepository.deleteFeedbackByid(feedbackId);

    }
}
