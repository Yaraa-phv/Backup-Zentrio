package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Checklist {
    private UUID checklistId;
    private String title;
    private String status;
    private String cover;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer checklistOrder;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private UUID taskId;
    private UUID createdBy;
}