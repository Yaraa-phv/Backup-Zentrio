package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.model.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper
public interface CommentRepository {
    @Select("""
        INSERT INTO comments(content,comment_date,checklist_id,commented_by_member)
        VALUES ( #{request.content},#{time} , #{checklistId} ,#{userId} )
           RETURNING *
        """)
    @Results( id = "commentMapper", value = {
            @Result(property = "id" , column = "comment_id"),
            @Result(property = "content", column = "content"),
            @Result(property = "creationDate" , column = "comment_date"),
            @Result(property = "checklistId" , column = "checklist_id"),
            @Result(property = "memberId" , column = "commented_by_member")
    })

    Comment createComment(UUID checklistId, @Param("request") CommentRequest commentRequest, LocalDateTime time, UUID userId);


    @Select("""
        INSERT INTO comments(content,comment_date,checklist_id,commented_by_leader)
        VALUES ( #{request.content},#{now} , #{checklistId} ,#{teamleadInchecklist} )
           RETURNING *
        """)
    @Results( id = "commentTeamleadMapper", value = {
            @Result(property = "id" , column = "comment_id"),
            @Result(property = "content", column = "content"),
            @Result(property = "creationDate" , column = "comment_date"),
            @Result(property = "checklistId" , column = "checklist_id"),
            @Result(property = "memberId" , column = "commented_by_leader")
    })

    Comment createCommentByleader(UUID checklistId, CommentRequest commentRequest, LocalDateTime now, UUID teamleadInchecklist);
}
