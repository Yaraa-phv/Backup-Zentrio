package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.RoleRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteRequest {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    private RoleRequest roleName;
}
