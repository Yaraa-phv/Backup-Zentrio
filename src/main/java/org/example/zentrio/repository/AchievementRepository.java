package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.AchievementRequest;
import org.example.zentrio.dto.response.MemberResponseData;
import org.example.zentrio.model.Achievement;
import org.example.zentrio.utility.JsonbTypeHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper
public interface AchievementRepository {

    @Results(id = "achievementMapper",value = {
            @Result(property = "achievementId",column = "achievement_id"),
            @Result(property = "userId",column = "user_id"),
            @Result(property = "details",column = "details",typeHandler = JsonbTypeHandler.class),
            @Result(property = "createdAt",column = "created_at"),
            @Result(property = "updatedAt",column = "updated_at"),
            @Result(property = "userDetails",column = "user_id",
            one = @One(select = "getDetailsOfUserByUserId")),
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
        SET details = #{req.details,jdbcType=OTHER, typeHandler= org.example.zentrio.utility.JsonbTypeHandler},
            updated_at = #{updatedAt}
        WHERE user_id = #{userId}
        RETURNING *
    """)
    @ResultMap("achievementMapper")
    Achievement updateAchievement(@Param("req") AchievementRequest achievementRequest, UUID userId, LocalDateTime updatedAt);

    @Select("""
        SELECT COUNT(*) FROM achievements WHERE user_id = #{userId}
    """)
    boolean isAchievementExist(UUID userId);

    @Select("""
        SELECT * FROM achievements WHERE user_id = #{userId}
    """)
    @ResultMap("achievementMapper")
    Achievement getAllAchievementByCurrentUser(UUID userId);

    @Select("""
        SELECT * FROM achievements
    """)
    @ResultMap("achievementMapper")
    Achievement getAllAchievements();

    @Select("""
        SELECT * FROM achievements
        WHERE achievement_id = #{achievementId}
    """)
    Achievement getAchievementById(UUID achievementId);


    @Select("""
        SELECT u.username, u.profile_image FROM users u
        WHERE u.user_id = #{userId}
    """)
    @Results(id = "userMapper",value = {
            @Result(property = "username",column = "username"),
            @Result(property = "imageUrl",column = "profile_image")
    })
    MemberResponseData getDetailsOfUserByUserId(UUID userId);

    @Select("""
        DELETE FROM achievements 
        WHERE achievement_id = #{achievementId}
        AND user_id = #{userId}
    """)
    void deletedAchievement(UUID achievementId, UUID userId);
}
