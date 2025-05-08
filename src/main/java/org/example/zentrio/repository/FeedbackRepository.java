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
        INSERT INTO  feedback (created_at ,comment , task_id, feedback_by)
        VALUES (#{time}, #{request.comment}, #{taskId} , #{userId} )
        RETURNING*
        """)
    @Results(id = "feedbackMapper", value = {
            @Result( property = "feedbakId",column = "feedback_id"),
            @Result( property = "createAt",column = "created_at"),
            @Result( property = "comment",column = "comment"),
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "userId", column = "feedback_by")
    })
    Feedback createFeedback(LocalDateTime time, UUID userId, UUID taskId, @Param("request") FeedbackRequest feedbackRequest);



    @Select("""
    SELECT * FROM feedback WHERE task_id=#{taskId}
    """)
    @ResultMap("feedbackMapper")
    List<Feedback> getAllFeedback(UUID taskId);



    @Select("""
        SELECT * FROM feedback WHERE feedback_id=#{feedbackId}
        """)
    @ResultMap("feedbackMapper")
    Feedback getFeedbackById(UUID feedbackId);



    @Select("""
        UPDATE feedback SET comment=#{request.comment}
                            WHERE feedback_id=#{feedbackId}
        RETURNING *
        """)
    @ResultMap("feedbackMapper")
    Feedback UpdateFeedbackByid(UUID feedbackId, @Param("request") FeedbackRequest feedbackRequest);


    @Select("""
    DELETE FROM feedback WHERE feedback_id=#{feedbackId}
    """)
    void deleteFeedbackByid(UUID feedbackId);
}
