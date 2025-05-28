package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private UUID boardId;

    @NotNull
    private UUID assigneeId;

    @NotNull
    private RoleRequest roleName;

    @NotNull
    private UUID workspaceId;
}
