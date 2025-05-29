package org.example.zentrio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.RoleName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleNameRequest {
    private RoleName roleName;
}
