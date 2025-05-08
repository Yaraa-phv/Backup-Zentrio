package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.model.Role;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RoleRepository {

//    @Select("""
//        SELECT * FROM roles
//    """)
//    @Results(id = "roleMapper", value = {
//            @Result(property = "roleId", column = "role_id"),
//            @Result(property = "roleName", column = "role_name")
//    })
//    List<Role> getAllRoles();

//    @Select("""
//        SELECT * FROM roles WHERE role_id = #{roleId}
//    """)
//    @ResultMap("roleMapper")
//    @Result(property = "roleId", column = "role_id")
//    Role getRoleNameByRoleId(UUID roleId);

//    @Select("""
//        SELECT * FROM roles WHERE role_name LIKE #{roleName}
//    """)
//    @ResultMap("roleMapper")
//    Role getRoleIdByRoleName(String roleName);

//    @Select("""
//        SELECT role_id FROM roles WHERE role_name = #{roleName}
//    """)
//    @ResultMap("roleMapper")
//    UUID getRoleId(String roleName);

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
    String getRoleNameByUserIdAndBoardId(UUID boardId, UUID userId);

    @Select("""
        SELECT member_id FROM members where board_id = #{boardId} AND user_id = #{assignedByUserId}
    """)
    UUID getMemberIdByUserIdAndBoardId(UUID boardId, UUID assignedByUserId);

    @Select("""
        SELECT role_name FROM roles WHERE role_id = #{roleId}
    """)
    String getRoleNameByRoleId(UUID roledId);


    @Select("""
            INSERT INTO members(user_id,board_id,role_id)
            VALUES(#{userId},#{boardId},#{roleId})
            """)
    void insertToMember(UUID userId, UUID boardId, UUID roleId);

}