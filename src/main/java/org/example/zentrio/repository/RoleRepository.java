package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.model.Role;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RoleRepository {

    @Select("""
        SELECT * FROM roles
    """)
    @Results(id = "roleMapper", value = {
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "roleName", column = "role_name")
    })
    List<Role> getAllRoles();

    @Select("""
        SELECT * FROM roles WHERE role_id = #{roleId}
    """)
    @ResultMap("roleMapper")
    @Result(property = "roleId", column = "role_id")
    Role getRoleNameByRoleId(UUID roleId);

    @Select("""
        SELECT * FROM roles WHERE role_name LIKE #{roleName}
    """)
    @ResultMap("roleMapper")
    Role getRoleIdByRoleName(String roleName);

    @Select("""
        SELECT role_id FROM roles WHERE role_name LIKE #{roleName}
    """)
    @ResultMap("roleMapper")
    UUID getRoleId(String roleName);
}