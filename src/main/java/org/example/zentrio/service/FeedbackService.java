package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.FeedbackRequest;
import org.example.zentrio.model.Feedback;

import java.util.List;
import java.util.UUID;

public interface FeedbackService {
    Feedback createFeedback( UUID taskId,  FeedbackRequest feedbackRequest);

    List<Feedback> getAllFeedback(UUID taskId);

    Feedback UpdateFeedbackByid(UUID feedbackId,  FeedbackRequest feedbackRequest);

    Feedback getFeedbackById(UUID feedbackId);

    void deleteFeedbackByid(UUID feedbackId);
}
