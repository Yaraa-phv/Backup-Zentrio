package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.AppUserRequest;
import org.example.zentrio.dto.request.ResetPasswordRequest;
import org.example.zentrio.dto.request.ThirdPartyRequest;
import org.example.zentrio.model.AppUser;

@Mapper
public interface AuthRepository {

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
    @Select("""
                INSERT INTO users (username, email, gender, password, profile_image)
                VALUES (
                        #{request.username},
                        #{request.email},
                        #{request.gender},
                        #{request.password},
                        #{request.profileImage}
                        )
                RETURNING *
            """)
    AppUser register(@Param("request") AppUserRequest appUserRequest);


    @ResultMap("UserMapper")
    @Select("""
        INSERT INTO users
        VALUES (default, #{request.fullName}, #{password},  #{request.email}, #{request.profilePicture})
        RETURNING *;
    """)
    AppUser registerThirdParty(@Param("request") ThirdPartyRequest request, String password);

    @ResultMap("UserMapper")
    @Select("""
        INSERT INTO users(email,username,password,profile_image,is_verified)
        VALUES(#{email},#{name},#{googleId},#{picture},#{isVerified})
        RETURNING *;
    """)
    void insertUser(String email, String name, String googleId, String picture,Boolean isVerified);

    @Select("""
        UPDATE users set password = #{req.password}, is_reset = true
        WHERE email = #{email}
    """)
    ResetPasswordRequest resetPassword(@Param("req") ResetPasswordRequest request,String email);
}
