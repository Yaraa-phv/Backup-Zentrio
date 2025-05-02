package org.example.zentrio.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.example.zentrio.model.AppUser;

@Mapper
public interface AppUserRepository {
    @Select("""
            SELECT * FROM users
            WHERE email= #{email}
            """)
    @Results(id = "UserMapper", value = {
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
    AppUser getUserByEmail(String email);

    @Select("""
        UPDATE users SET is_verified = true
    """)
    void updateVerify();
}
