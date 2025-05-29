package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.ProfileRequest;
import org.example.zentrio.model.AppUser;

import java.util.UUID;

@Mapper
public interface ProfileRepository {

    @Results(id = "profileMapper", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "password", column = "password"),
            @Result(property = "profileImage", column = "profile_image"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "isReset", column = "is_reset"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    }
    )
    @Select("""
        UPDATE users set username = #{req.username},
                        profile_image = #{req.profileImageUrl}
        WHERE user_id = #{appUserId}
        RETURNING *
    """)
    AppUser updateProfile(@Param("req") ProfileRequest profileRequest, UUID appUserId);

    @ResultMap("profileMapper")
    @Select("""
        DELETE FROM users WHERE user_id = #{appUserId}
        RETURNING *
    """)
    void deleteProfile(UUID appUserId);
}
