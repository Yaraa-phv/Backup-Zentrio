package org.example.zentrio.repository;

import org.apache.ibatis.annotations.Mapper;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.model.Comment;

import java.util.UUID;

@Mapper
public interface CommentRepository {
    Comment createComment(UUID checklistId, CommentRequest commentRequest);

}
