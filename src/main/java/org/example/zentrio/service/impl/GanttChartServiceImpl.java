package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.repository.GanttChartRepository;
import org.example.zentrio.service.BoardService;
import org.example.zentrio.service.GanttChartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class GanttChartServiceImpl implements GanttChartService {

    private final GanttChartRepository ganttChartRepository;
    private final BoardService boardService;

    @Override
    public GanttChart createGanttChartByBoardId(GanttChartRequest ganttChartRequest, UUID boardId) {
        boardService.getBoardByBoardId(boardId);
        GanttChart existingGanttChart = ganttChartRepository.getGanttChartByBoardId(boardId);

        if (existingGanttChart != null) {
            throw new NotFoundException("Gantt chart for board id " + boardId + " already created");
        }

        // Create a new Gantt Chart
        LocalDateTime now = LocalDateTime.now();
        GanttChart ganttChart = new GanttChart();
        ganttChart.setTitle(ganttChartRequest.getTitle());
        ganttChart.setBoardId(boardId);
        ganttChart.setCreatedAt(now);
        ganttChart.setUpdatedAt(now);

        return ganttChartRepository.createGanttChartByBoardId(ganttChartRequest, boardId);
    }

    @Override
    public GanttChart getGanttChartById(UUID ganttChartId) {
        GanttChart ganttChart = ganttChartRepository.getGanttChartById(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("Gantt chart with " + ganttChartId + " not found");
        }
        return ganttChart;
    }

    @Override
    public GanttChart deleteGanttChartById(UUID ganttChartId) {
        getGanttChartById(ganttChartId);
        return ganttChartRepository.deleteGanttChartById(ganttChartId);
    }

    @Override
    public GanttChart getAllGanttChartByBoardId(UUID boardId) {
        boardService.getBoardByBoardId(boardId);
        return ganttChartRepository.getAllGanttChartByBoardId(boardId);
    }

    @Override
    public GanttChart updateGanttChartByGanttChartId(GanttChartRequest ganttChartRequest, UUID ganttChartId) {
        getGanttChartById(ganttChartId);
        return ganttChartRepository.updateGanttChartByGanttChartId(ganttChartRequest, ganttChartId);
    }


}
