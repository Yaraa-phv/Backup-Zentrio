package org.example.zentrio.service;


import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.model.GanttChart;

import java.util.List;
import java.util.UUID;

public interface GanttChartService {
    GanttChart createGanttChart(UUID boardId, GanttChartRequest ganttChartRequest);

    GanttChart getGanttChartByBoardId(UUID boardId);

    GanttChart updateGannntChartById(UUID ganttChartId, GanttChartRequest ganttChartRequest);

    Void deleteGanttChartByID(UUID ganttChartId);

    GanttChart getGanttChartByID(UUID ganttChartId);


}
