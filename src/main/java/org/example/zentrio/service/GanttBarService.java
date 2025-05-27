package org.example.zentrio.service;



import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.model.GanttBar;

import java.util.HashSet;
import java.util.UUID;

public interface GanttBarService {
    GanttBar createGanttBarByGanttChartId(GanttBarRequest ganttBarRequest);

    GanttBar getGanttBarById(UUID ganttBarId, UUID ganttChartId);

    HashSet<GanttBar> getAllGanttBarsByGanttChartId(UUID ganttChartId);

    GanttBar updateGanttBarByGanttBarId(GanttBarRequest ganttBarRequest, UUID ganttBarId);

    void deleteGanttBarByGanttBarId(UUID ganttBarId,UUID ganttChartId);


}
