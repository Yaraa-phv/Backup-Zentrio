package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.dto.response.MemberResponse;
import org.example.zentrio.dto.response.MemberResponseData;
import org.example.zentrio.model.Comment;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Mapper
public interface CommentRepository {
    @Select("""
        INSERT INTO comments(content,comment_date,checklist_id,commented_by)
        VALUES ( #{request.content},#{time} , #{checklistId} ,#{userId} )
           RETURNING *
        """)
    @Results( id = "commentMapper", value = {
            @Result(property = "id" , column = "comment_id"),
            @Result(property = "content", column = "content"),
            @Result(property = "creationDate" , column = "comment_date"),
            @Result(property = "checklistId" , column = "checklist_id"),
            @Result(property = "commentBy" , column = "commented_by"),
            @Result(property = "member" , column = "commented_by",
            one = @One(select = "getMemberByUserId" ))
    })

    Comment createComment(UUID checklistId, @Param("request") CommentRequest commentRequest, LocalDateTime time, UUID userId);


    @Select("""
        SELECT  * FROM  comments 
        WHERE comment_id= #{commentId}
        """)
    @ResultMap("commentMapper")
    Comment getCommentByCommentId(UUID commentId);

    @Select("""
        SELECT  * FROM  comments 
        WHERE checklist_id= #{checkListId}
        """)
    @ResultMap("commentMapper")
    Set<Comment> getAllComments(UUID checkListId);

    @Select("""
        DELETE FROM comments  WHERE comment_id= #{commentId}
        RETURNING *
        """)
    void deleteCommentByCommentId(UUID commentId);

    @Select("""
        UPDATE  comments 
        SET content= #{request.content}
        WHERE comment_id =#{commentId}
        RETURNING *
        """)
    @ResultMap("commentMapper")
    Comment UpdateCommentByCommentId(UUID commentId,@Param("request") CommentRequest commentRequest);

    @Select("""
        
         SELECT u.profile_image AS image , u.username AS name FROM users u
        INNER JOIN  members m ON u.user_id = m.user_id
        WHERE member_id= #{memberId}
        """)
    @Results(id = "memberResponseMapper", value = {
            @Result(property = "imageUrl", column = "image"),
            @Result(property = "username", column = "name"),
    })
    MemberResponseData getMemberByUserId(UUID memberId);


}
