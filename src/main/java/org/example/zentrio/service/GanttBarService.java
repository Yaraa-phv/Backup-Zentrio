package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.model.GanttBar;

import java.util.List;
import java.util.UUID;

public interface GanttBarService {
    GanttBar createGanttBar(UUID ganttBarId, GanttBarRequest ganttBarRequest);

    List<GanttBar> getAllGanttBarByGanttChartID(UUID ganttChartId);


    GanttBar getGanttBarByGanttBartID(UUID ganttBarId);


    GanttBar updateGanttBarByGanttBarId(UUID ganttBarId,  GanttBarRequest ganttBarRequest);

    void deleteGanttBarByGanttBarId(UUID ganttBarId);

    GanttBar getGanttBarByGanttChartIdAndGanttBarId(UUID ganttChartId, UUID ganttBarId);

}
