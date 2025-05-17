package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.ManagerRequest;
import org.example.zentrio.model.Member;

import java.util.HashMap;
import java.util.List;
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

    @Select("""
        SELECT * FROM members WHERE board_id = #{boardId}
    """)
    @ResultMap("memberMapper")
    List<Member> getAllMembersByBoardId(UUID boardId);

    @Select("""
        SELECT member_id FROM members WHERE board_id = #{boardId} AND member_id = #{memberId}
    """)
    UUID getMemberIdByBoardIdAndMemberId(UUID boardId, UUID memberId);

    @Select("""
        UPDATE members SET role_id = #{roleId} WHERE member_id = #{memberId}
        RETURNING*
    """)
    @ResultMap("memberMapper")
    Member editRoleForMembersByBoardIdAndMemberId(UUID memberId, UUID roleId);


    @Select("""
        
        SELECT r.role_name FROM roles r
        INNER JOIN  members m ON r.role_id = m.role_id
        WHERE  m.user_id= #{userId}
        AND  m.board_id= #{boardId}
        
        """)
    String getRoleByBoardIdAndUserId(UUID boardId, UUID userId);
}