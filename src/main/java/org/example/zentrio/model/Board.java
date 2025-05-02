package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private UUID boardId;
    private String title;
    private String description;
    private String cover;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isFavourite;
    private Boolean isVerified;
    private UUID workspaceId;
}