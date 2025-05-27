package org.example.zentrio.service;



import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.model.GanttChart;

import java.util.UUID;

public interface GanttChartService {

    GanttChart createGanttChartByBoardId(GanttChartRequest ganttChartRequest);

    GanttChart getAllGanttChartByBoardId(UUID boardId);

    GanttChart updateGanttChartByGanttChartId(GanttChartRequest ganttChartRequest, UUID ganttChartId);

    GanttChart getGanttChartById(UUID ganttChartId,UUID boardId);

    void deleteGanttChartById(UUID ganttChartId,UUID boardId);
}
