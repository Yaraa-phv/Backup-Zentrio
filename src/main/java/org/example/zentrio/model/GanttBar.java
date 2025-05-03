package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GanttBar {

    private UUID ganttBarId;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime finishedAt;
    private UUID ganttChartId;

}
