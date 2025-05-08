package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private UUID taskId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Boolean isDone;
    private Integer taskOrder;
    private String stage;
    private UUID boardId;
    private UUID ganttBarId;
    private String ganttBarTitle;
//    private Board boardId;
//    private GanttBar ganttBarId;
}