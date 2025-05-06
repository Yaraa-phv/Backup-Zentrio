package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.ManagerRequest;
import org.example.zentrio.model.Member;

import java.util.UUID;

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

    @Select("""
        SELECT role_id FROM members WHERE user_id = #{userId}
    """)
//    @ResultMap("memberMapper")
    UUID getRoleIdByUserIdAsAMember(UUID userId);

    @Select("""
        SELECT role_id FROM members WHERE member_id = #{memberId}
    """)
    UUID getRoleIdByMemberId(UUID memberId);

    @Select("""
        SELECT member_id FROM members WHERE user_id = #{userId} AND board_id = #{boardId}
    """)
//    @ResultMap("memberMapper")
    UUID getMemberIdByUserIdAndBoardId(UUID userId, UUID boardId);

    @Select("""
        SELECT * FROM members WHERE user_id = #{userId} AND board_id = #{boardId}
    """)
    @ResultMap("memberMapper")
    Member getMemberByUserIdAndBoardId(UUID userId, UUID boardId);
}