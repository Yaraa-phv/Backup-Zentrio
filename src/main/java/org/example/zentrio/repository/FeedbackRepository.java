package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.FeedbackRequest;
import org.example.zentrio.model.Feedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface FeedbackRepository {

    @Select("""
        INSERT INTO  feedbacks (created_at ,comment , task_id, feedback_by)
        VALUES (#{time}, #{request.comment}, #{taskId} , #{userId} )
        RETURNING*
        """)
    @Results(id = "feedbackMapper", value = {
            @Result( property = "feedbackId",column = "feedback_id"),
            @Result( property = "createAt",column = "created_at"),
            @Result( property = "comment",column = "comment"),
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "userId", column = "feedback_by"),
            @Result(property = "user", column = "feedback_by",
            one = @One(select = "org.example.zentrio.repository.CommentRepository.getMemberByUserId"))
    })
    Feedback createFeedback(LocalDateTime time, UUID userId, UUID taskId, @Param("request") FeedbackRequest feedbackRequest);



    @Select("""
    SELECT * FROM feedbacks WHERE task_id=#{taskId}
    """)
    @ResultMap("feedbackMapper")
    List<Feedback> getAllFeedback(UUID taskId);



    @Select("""
        SELECT * FROM feedbacks WHERE feedback_id= #{feedbackId} AND task_id= #{taskId}
        """)
    @ResultMap("feedbackMapper")
    Feedback getFeedbackById(UUID feedbackId,  UUID taskId);



    @Select("""
        UPDATE feedbacks SET comment=#{request.comment}
                            WHERE feedback_id=#{feedbackId}
        RETURNING *
        """)
    @ResultMap("feedbackMapper")
    Feedback UpdateFeedbackById(UUID feedbackId, @Param("request") FeedbackRequest feedbackRequest);


    @Select("""
    DELETE FROM feedbacks WHERE feedback_id=#{feedbackId}
    """)
    void deleteFeedbackById(UUID feedbackId);
}
