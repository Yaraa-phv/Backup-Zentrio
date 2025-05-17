package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.exception.BadRequestException;
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
    public GanttBar  createGanttBar(UUID ganttChartId , GanttBarRequest ganttBarRequest) {
       GanttChart ganttChart = ganttChartService.getGanttChartByID(ganttChartId);
       if (ganttChart == null) {
           throw new NotFoundException("GanttChart not found");
       }
        if (ganttBarRequest.getStartedAt() == null || ganttBarRequest.getFinishedAt() == null) {
            throw new BadRequestException("Start and finish times are required");
        }
       if (ganttBarRequest.getFinishedAt().isBefore(ganttBarRequest.getStartedAt())){
           throw new BadRequestException("GanttBar already finished");
       }
       ganttChartService.userRole(ganttChart.getBoard_id());

        return ganttBarRepository.createGanttBar(ganttChartId,ganttBarRequest);
    }



    @Override
    public List<GanttBar> getAllGanttBarByGanttChartID(UUID ganttChartId) {
        GanttChart ganttChart= ganttChartService.getGanttChartByID(ganttChartId);
        if (ganttChart == null) {
            throw new NotFoundException("GanttChart not found");
        }

        return ganttBarRepository.getAllGanttBarByGanttChartID(ganttChartId);
    }

    @Override
    public GanttBar getGanttBarByGanttBartID(UUID ganttBarId) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBartID(ganttBarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        return ganttBar;
    }

    @Override
    public GanttBar updateGanttBarByGanttBarId(UUID ganttBarId, GanttBarRequest ganttBarRequest) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBartID(ganttBarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        if (ganttBarRequest.getFinishedAt().isBefore(ganttBarRequest.getStartedAt())){
            throw new BadRequestException("GanttBar already finished");
        }
        GanttChart chart= ganttChartService.getGanttChartByID(ganttBar.getGanttChartId());
        ganttChartService.userRole(chart.getBoard_id());

        GanttBar updatedGanttBar=ganttBarRepository.updateGanttBarByGanttBarId(ganttBarId,ganttBarRequest);
        return updatedGanttBar;
    }

    @Override
    public void deleteGanttBarByGanttBarId(UUID ganttBarId) {
        GanttBar ganttBar= ganttBarRepository.getGanttBarByGanttBartID(ganttBarId);
        if (ganttBar == null) {
            throw new NotFoundException("GanttBar not found");
        }
        GanttChart chart= ganttChartService.getGanttChartByID(ganttBar.getGanttChartId());
        ganttChartService.userRole(chart.getBoard_id());

        ganttBarRepository.deleteGanttBarByGanttBarId(ganttBarId);
    }

    @Override
    public GanttBar getGanttBarByGanttChartIdAndGanttBarId(UUID ganttChartId, UUID ganttBarId) {

        if (ganttBarId == null){
            throw new NotFoundException("Gantt Bar Id not found!");
        }
        return ganttBarRepository.getGanttBarByGanttChartIdAndGanttBarId(ganttChartId, ganttBarId);
    }

}
