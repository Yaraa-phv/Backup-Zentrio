package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.dto.request.GanttBarRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GanttBar {

    private UUID ganttBarId;
    private String title;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private UUID ganttChartId;


    public GanttBarRequest mapToGanttBarRequest() {
        return GanttBarRequest.builder()
                .title(this.getTitle())
                .startedAt(this.getStartedAt())
                .finishedAt(this.getFinishedAt())
                .build();
    }


}
