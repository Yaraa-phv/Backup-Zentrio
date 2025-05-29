package org.example.zentrio.service;

import org.example.zentrio.dto.request.FeedbackRequest;
import org.example.zentrio.model.Feedback;

import java.util.Set;
import java.util.UUID;

public interface FeedbackService {
    Feedback createFeedback(   FeedbackRequest feedbackRequest);

    Set<Feedback> getAllFeedback(UUID taskId);

    Feedback UpdateFeedbackById(UUID feedbackId, FeedbackRequest feedbackRequest);

    Feedback getFeedbackById(UUID feedbackId , UUID taskId);

    void deleteFeedbackById(UUID feedbackId, UUID taskId);
}
