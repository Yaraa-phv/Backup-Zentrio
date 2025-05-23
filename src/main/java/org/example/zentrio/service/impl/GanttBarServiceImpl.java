package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.repository.GanttBarRepository;
import org.example.zentrio.service.GanttBarService;
import org.example.zentrio.service.GanttChartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GanttBarServiceImpl implements GanttBarService {

    private final GanttBarRepository ganttBarRepository;
    private final GanttChartService ganttChartService;

    @Override
    public GanttBar createGanttBarByGanttChartId(GanttBarRequest ganttBarRequest, UUID ganttChartId) {
        ganttChartService.getGanttChartById(ganttChartId);
        return ganttBarRepository.createGanttBarByGanttChartId(ganttBarRequest, ganttChartId);
    }

    @Override
    public GanttBar getGanttBarById(UUID ganttBarId) {
        GanttBar ganttBar = ganttBarRepository.getGanttBarById(ganttBarId);
        if (ganttBar == null) {
            throw new BadRequestException("Gantt bar with id " + ganttBarId + " not found");
        }
        return ganttBar;
    }

    @Override
    public List<GanttBar> getAllGanttBarsByGanttChartId(UUID ganttChartId) {
        ganttChartService.getGanttChartById(ganttChartId);
        return ganttBarRepository.getAllGanttBarsByGanttChartId(ganttChartId);
    }

    @Override
    public GanttBar updateGanttBarByGanttBarId(GanttBarRequest ganttBarRequest, UUID ganttBarId) {
        getGanttBarById(ganttBarId);
        return ganttBarRepository.updateGanttBarByGanttBarId(ganttBarRequest,ganttBarId);
    }

    @Override
    public GanttBar deleteGanttBarByGanttBarId(UUID ganttBarId) {
        getGanttBarById(ganttBarId);
        return ganttBarRepository.deleteGanttBarByGanttBarId(ganttBarId);
    }
}
