package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.AchievementRequest;
import org.example.zentrio.model.Achievement;
import org.example.zentrio.utility.JsonbTypeHandler;

import java.util.UUID;

@Mapper
public interface AchievementRepository {

    @Results(id = "achievementMapper",value = {
            @Result(property = "achievementId",column = "achievement_id"),
            @Result(property = "userId",column = "user_id"),
            @Result(property = "details",column = "details",typeHandler = JsonbTypeHandler.class),
            @Result(property = "createdAt",column = "created_at"),
            @Result(property = "updatedAt",column = "updated_at"),
    })

    @Select("""
        INSERT INTO achievements (details,user_id)
        VALUES(
               #{req.details,jdbcType=OTHER, typeHandler = org.example.zentrio.utility.JsonbTypeHandler},
               #{userId})
        RETURNING *
    """)
    Achievement createAchievement(@Param("req") AchievementRequest achievementRequest, UUID userId);

    @Select("""
        UPDATE achievements
        SET details = #{req.details,jdbcType=OTHER, typeHandler= org.example.zentrio.utility.JsonbTypeHandler}
        WHERE user_id = #{userId}
        RETURNING *
    """)
    @ResultMap("achievementMapper")
    Achievement updateAchievement(@Param("req") AchievementRequest achievementRequest, UUID userId);

    @Select("""
        SELECT COUNT(*) FROM achievements WHERE user_id = #{userId}
    """)
    boolean isAchievementExist(UUID userId);

    @Select("""
        SELECT * FROM achievements WHERE user_id = #{userId}
    """)
    @ResultMap("achievementMapper")
    Achievement getAllAchievementByCurrentUser(UUID userId);
}
