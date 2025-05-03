package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.model.GanttChart;
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
    public GanttBar creatGanntBar(UUID ganntChartId , GanttBarRequest ganttBarRequest) {
       GanttChart ganttChart = ganttChartService.getGanttChartByID(ganntChartId);
       if (ganttChart == null) {
           throw new NotFoundException("GanttChart not found");
       }

        return ganttBarRepository.creatGanntBar(ganntChartId,ganttBarRequest);
    }



    @Override
    public List<GanttBar> getAllGanttBarByGanttChartID(UUID ganntChartId) {
        GanttChart ganttChart= ganttChartService.getGanttChartByID(ganntChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart not found");
        }

        return ganttBarRepository.getAllGanttBarByGanttChartID(ganntChartId);
    }

    @Override
    public GanttBar getGanttBarByGanttBartID(UUID geanntbarId) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBartID(geanntbarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        return ganttBar;
    }

    @Override
    public GanttBar updateGanttBarByGanttBarId(UUID ganntbarId, GanttBarRequest ganttBarRequest) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBartID(ganntbarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        GanttBar updatedGanntBar=ganttBarRepository.updateGanttBarByGanttBarId(ganntbarId,ganttBarRequest);
        return updatedGanntBar;
    }

    @Override
    public void deleteGanttBarByGanttBarId(UUID geanttbarId) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBartID(geanttbarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        ganttBarRepository.deleteGanttBarByGanttBarId(geanttbarId);
    }

    @Override
    public GanttBar getGanttBarByGanttChartIdAndGanttBarId(UUID ganttChartId, UUID ganttBarId) {

        if (ganttBarId == null){
            throw new NotFoundException("Gantt Bar Id not found!");
        }
        return ganttBarRepository.getGanttBarByGanttChartIdAndGanttBarId(ganttChartId, ganttBarId);
    }
}
