package org.example.zentrio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private UUID userId;
    private String username;
    private String email;
    private String profileImage;
    private List<String> roles;
}
