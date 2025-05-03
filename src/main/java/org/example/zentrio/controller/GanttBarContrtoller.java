package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.service.GanttBarService;
import org.example.zentrio.service.GanttChartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/Gantt-bars")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gantt-bars Controller")
public class GanttBarContrtoller {

    private final GanttBarService ganttBarService;

    @PostMapping("/{ganntChartId}")
    @Operation(summary = "Creat Gannt-bar by Gannt-Chart Id")
    public ResponseEntity<ApiResponse<GanttBar>> creatGanntBar(
            @PathVariable("ganntChartId") UUID ganntChartId, @Valid @RequestBody GanttBarRequest ganttBarRequest) {

        ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Creat Gannt-Bar successfully.")
                .payload(ganttBarService.creatGanntBar(ganntChartId,ganttBarRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("gantt-chart-id/{ganntChartId}")
    @Operation(summary = "Get Gannt-bars by Gantt-Chart Id")
    public ResponseEntity<ApiResponse<List<GanttBar>>> getAllGanttBarByGanttChartID(@PathVariable("ganntChartId") UUID ganntChartId){

        ApiResponse<List<GanttBar>> response = ApiResponse.<List<GanttBar>>builder()
                .success(true)
                .message("Get all Gannt-bars by Gantt-chartId Successfully")
                .payload(ganttBarService.getAllGanttBarByGanttChartID(ganntChartId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/gantt-barid/{geanntbarId}")
    @Operation(summary = "Get Gantt-bars by Gantt-bar Id")
    public ResponseEntity<ApiResponse<GanttBar>> getGanttBarByGanttBartID(@PathVariable("geanntbarId") UUID geanntbarId){

        ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Get all Gannt-bars by Gantt-chartId Successfully")
                .payload( ganttBarService.getGanttBarByGanttBartID(geanntbarId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


        @PutMapping("/gantt-barid/{geanntbarId}")
        @Operation(summary = "Update Gantt-bar by Gantt-barId")
        public ResponseEntity<ApiResponse<GanttBar>> updateGanttBarByGanttBarId(
                @PathVariable("geanntbarId") UUID geanntbarId,  @Valid @RequestBody GanttBarRequest ganttBarRequest){

            ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                    .success(true)
                    .message("Update Gannt-bars by Gantt-barId Successfully")
                    .payload( ganttBarService.updateGanttBarByGanttBarId(geanntbarId, ganttBarRequest))
                    .status(HttpStatus.CREATED)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(response);
        }


    @DeleteMapping("/gantt-barid/{geanntbarId}")
    @Operation(summary = "Delete Gantt-bar by Gantt-barId")
    public ResponseEntity<ApiResponse<GanttBar>> deleteGanttBarByGanttBarId(
            @PathVariable("geanntbarId") UUID geanttbarId){
        ganttBarService.deleteGanttBarByGanttBarId(geanttbarId);
        ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Delete Gannt-bars by Gantt-barId Successfully")
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
