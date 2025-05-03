package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private UUID memberId;
    private UUID roleId;
    private UUID userId;
    private UUID boardId;
}
