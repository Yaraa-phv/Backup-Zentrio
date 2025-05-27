package org.example.zentrio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.RoleName;
import org.example.zentrio.enums.RoleRequest;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignedRoleRequest {
    private UUID boardId;
    private UUID assigneeId;
    private RoleRequest roleName;
    private UUID workspaceId;
}
