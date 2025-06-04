package org.example.zentrio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.RoleName;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSummary {
    private UUID boardId;
    private String boardName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID workspaceId;
    private String workspaceName;
    private RoleName role;
}
