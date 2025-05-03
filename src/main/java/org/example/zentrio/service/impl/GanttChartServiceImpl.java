package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.repository.GanttChartRepository;
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


    @Override
    public GanttChart createGanttChart(UUID boardId, GanttChartRequest ganttChartRequest) {
        return  ganttChartRepository.createGanttChart(boardId, ganttChartRequest, LocalDateTime.now());
    }

    @Override
    public List<GanttChart> getAllGanttChartByBoardId(UUID boardId) {
        return ganttChartRepository.getAllGanttChartByBoardId(boardId);
    }

    @Override
    public GanttChart updateGannntCjhartById(UUID ganttChartId, GanttChartRequest ganttChartRequest) {
        return  ganttChartRepository.updateGannntCjhartById(ganttChartId, ganttChartRequest, LocalDateTime.now());
    }

    @Override
    public Void deleteGanttChartByID(UUID ganttChartId) {
        GanttChart ganttChart= ganttChartRepository.getGanttChartById(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart id:"+ganttChartId+" not found");
        }

        return ganttChartRepository.deleteGanttChartByID(ganttChartId);
    }

    @Override
    public GanttChart getGanttChartByID(UUID ganttChartId) {
        GanttChart ganttChart= ganttChartRepository.getGanttChartById(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart id:"+ganttChartId+" not found");
        }
        return  ganttChart;
    }
}
