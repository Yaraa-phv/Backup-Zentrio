package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.dto.response.AppUserResponse;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private UUID memberId;
    private AppUserResponse userResponse;
    private String roles;
}
