package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.dto.response.MemberResponse;
import org.example.zentrio.dto.response.MemberResponseData;

import java.time.LocalDateTime;
import java.util.List;
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
    private String face;
    private List<MemberResponseData> teamLeader;

}
