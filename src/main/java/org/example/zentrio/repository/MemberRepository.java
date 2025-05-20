package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.ManagerRequest;
import org.example.zentrio.model.Member;

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
                                              INNER JOIN  members m ON m.role_id = r.role_id
                  WHERE  m.user_id= #{userId}
                    AND  m.role_id= '34e22ec4-0898-44db-acf5-1c3ae5f8ef25'
                    AND  m.board_id= #{boardId} limit 1;
        
        """)
    String getRolePMByBoardIdAndUserId(UUID boardId, UUID userId);


    @Select("""
        
            SELECT  m.member_id   from members m
              inner join task_assignment task on task.assigned_to =m.member_id
        where m.user_id= #{uuid}
          and  task.task_id= #{taskId}
        """)
    UUID getTeamleadUUID(UUID uuid, UUID taskId);


    @Select("""
        SELECT member_id FROM members 
                         WHERE user_id = #{userId} AND board_id = #{boardId}
                        AND role_id= '58ad541b-2dea-4a64-a2e2-d40835abba95'
    """)
//    @ResultMap("memberMapper")
    UUID getMemberId(UUID userId, UUID boardId);



    @Select("""
            SELECT member_id FROM members 
                         WHERE user_id = #{userId} AND board_id = #{boardId}
                        AND role_id= '34e22ec4-0898-44db-acf5-1c3ae5f8ef25'
        """)
    UUID getPmId(UUID userId, UUID boardId);



    @Select("""
        
            SELECT  roles.role_name from roles
        inner join public.members m on roles.role_id = m.role_id
        inner join  task_assignment tk on tk.assigned_to =m.member_id
                                OR tk.assigned_by = member_id
        where m.user_id= #{userId}
          AND  tk.task_id= #{taskId}
        AND  m.board_id= #{boardId} limit  1;
        """)
    String getRoleInTask(UUID boardId, UUID userId, UUID taskId);
}