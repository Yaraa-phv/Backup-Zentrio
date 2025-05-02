package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.ManagerRequest;
import org.example.zentrio.model.Member;

@Mapper
public interface MemberRepository {

//    @Select("""
//        INSERT INTO members(role_id, user_id, board_id)
//        VALUES (#{roleId}, #{userId}, #{request.boardId})
//        RETURNING *
//    """)
    ////    @Results(id = "memberMapper", value = {
    ////            @Result(property = "memberId", column = "member_id"),
    ////            @Result(property = "roleId", column = "role_id"),
    ////            @Result(property = "userId", column = "user_id"),
    ////            @Result(property = "boardId", column = "board_id"),
    ////    })
    ////    @Result(property = "roleId", column = "role_id")
//    Member insertManagerToBoard(@Param("request") MemberRequest memberRequest, UUID roleId, UUID userId);

    @Select("""
        INSERT INTO members(role_id, user_id, board_id)
        VALUES (#{request.roleId}, #{request.userId}, #{request.boardId})
        RETURNING *
    """)
    @Results(id = "memberMapper", value = {
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "boardId", column = "board_id"),
    })
    Member insertManagerToBoard(@Param("request") ManagerRequest managerRequest);
}