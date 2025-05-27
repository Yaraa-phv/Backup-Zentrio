package org.example.zentrio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.RoleName;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignedRoleRequest {
    private UUID boardId;
    private UUID assigneeId;
    private RoleName roleName;
    private UUID workspaceId;
}
