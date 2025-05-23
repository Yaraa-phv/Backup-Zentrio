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
public class GanttChart {
    private UUID ganttChartId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID boardId;
}