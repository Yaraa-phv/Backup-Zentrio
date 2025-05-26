package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.model.Comment;

import java.util.Set;
import java.util.UUID;

public interface CommentService {
    Comment createComment(UUID checklistId,  CommentRequest commentRequest);


    Comment getCommentByCommentId(UUID commentId);

    Set<Comment> getAllComments(UUID checkListId);

    Void deleteCommentByCommentId(UUID commentId);


    Comment UpdateCommentByCommentId(UUID commentId,  CommentRequest commentRequest);
}
