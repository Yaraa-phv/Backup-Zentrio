package org.example.zentrio.service;

import org.example.zentrio.enums.RoleName;
import org.example.zentrio.model.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    Role getRoleNameByRoleId(UUID roleId);

    List<Role> getAllRoles();

    Role getRoleIdByRoleName(RoleName roleName);

    UUID getRoleId(RoleName roleName);
}

