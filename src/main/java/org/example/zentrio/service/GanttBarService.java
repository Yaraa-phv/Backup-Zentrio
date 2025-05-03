package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.model.GanttBar;

import java.util.List;
import java.util.UUID;

public interface GanttBarService {
    GanttBar creatGanntBar(UUID ganntBartId, GanttBarRequest ganttBarRequest);

    List<GanttBar> getAllGanttBarByGanttChartID(UUID ganntChartId);


    GanttBar getGanttBarByGanttBartID(UUID geanntbarId);


    GanttBar updateGanttBarByGanttBarId(UUID geanntbarId,  GanttBarRequest ganttBarRequest);

    void deleteGanttBarByGanttBarId(UUID geanttbarId);
}
