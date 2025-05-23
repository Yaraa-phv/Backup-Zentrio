package org.example.zentrio.service;



import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.model.GanttBar;

import java.util.List;
import java.util.UUID;

public interface GanttBarService {
    GanttBar createGanttBarByGanttChartId(GanttBarRequest ganttBarRequest, UUID ganttChartId);

    GanttBar getGanttBarById(UUID ganttBarId);

    List<GanttBar> getAllGanttBarsByGanttChartId(UUID ganttChartId);

    GanttBar updateGanttBarByGanttBarId(GanttBarRequest ganttBarRequest, UUID ganttBarId);

    GanttBar deleteGanttBarByGanttBarId(UUID ganttBarId);


}
