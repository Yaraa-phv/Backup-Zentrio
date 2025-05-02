package org.example.zentrio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequest {
    private UUID userId;
    private UUID boardId;
    private UUID roleId;
//    @NotBlank(message = "Email cannot be blank!")
//    @Email(message = "Email better follow its format!")
//    private String email;
//    @NotBlank(message = "Board Id cannot be blank!")
//    private UUID boardId;
//    @NotBlank(message = "Role name cannot be blank!")
//    private RoleName roleName;
}
