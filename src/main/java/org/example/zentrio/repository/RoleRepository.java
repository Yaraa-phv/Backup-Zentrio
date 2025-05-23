package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RoleRepository {

    @Select("""
                SELECT role_id FROM roles WHERE role_name = #{roleName}
            """)
    UUID getRoleIdByRoleName(String roleName);

    @Select("""
                SELECT r.role_name FROM roles r INNER JOIN members m
                ON r.role_id = m.role_id
                WHERE board_id = #{boardId}
                AND user_id = #{userId}
            """)
    String getRoleNameByBoardIdAndUserId(UUID boardId, UUID userId);



    @Select("""
            INSERT INTO members(user_id,board_id,role_id)
            VALUES(#{userId},#{boardId},#{roleId})
            """)
    void insertToMember(UUID userId, UUID boardId, UUID roleId);


    @Select("""
                SELECT r.role_name FROM members m
                INNER JOIN roles r ON r.role_id = m.role_id
                WHERE m.board_id = #{boardId} AND m.user_id = #{userId}
            """)
    List<String> getRolesNameByBoardIdAndUserId(UUID boardId, UUID userId);



    @Select("""
          SELECT r.role_name FROM roles r INNER JOIN members m
                ON r.role_id = m.role_id
                WHERE board_id = #{boardId}
                AND user_id = #{userId}
                AND r.role_name = 'ROLE_LEADER'
    """)
    String getRoleLeaderNameByBoardIdAndUserId(UUID boardId, UUID userId);
}