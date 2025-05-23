package org.example.zentrio.service;


import jakarta.validation.Valid;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.model.GanttChart;

import java.util.List;
import java.util.UUID;

public interface GanttChartService {
//    GanttChart createGanttChart(UUID boardId, GanttChartRequest ganttChartRequest);
//
//    GanttChart getGanttChartByBoardId(UUID boardId);
//
//    GanttChart updateGannntCjhartById(UUID ganttChartId, GanttChartRequest ganttChartRequest);
//
//    Void deleteGanttChartByID(UUID ganttChartId);
//
//    GanttChart getGanttChartByID(UUID ganttChartId);


    GanttChart createGanttChartByBoardId(GanttChartRequest ganttChartRequest, UUID boardId);

    GanttChart getAllGanttChartByBoardId(UUID boardId);

    GanttChart updateGanttChartByGanttChartId(GanttChartRequest ganttChartRequest, UUID ganttChartId);

    GanttChart getGanttChartById(UUID ganttChartId);

    GanttChart deleteGanttChartById(UUID ganttChartId);
}
