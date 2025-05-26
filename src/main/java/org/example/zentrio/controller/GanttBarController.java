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
@RequestMapping("api/v1/gantt-bars")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gantt-bars Controller")
public class GanttBarController {

    private final GanttBarService ganttBarService;

    @PostMapping("/{gantt-chart-id}")
    @Operation(summary = "Creat gantt bar by gantt chart ID")
    public ResponseEntity<ApiResponse<GanttBar>> createGanttBar(
            @PathVariable("gantt-chart-id") UUID ganttChartId, @Valid @RequestBody GanttBarRequest ganttBarRequest) {

        ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Creat gantt bar successfully.")
                .payload(ganttBarService.createGanttBar(ganttChartId,ganttBarRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(response);
    }



    @GetMapping("/gantt-charts/{gantt-chart-id}")
    @Operation(summary = "Get all gantt bars by gantt chart ID")
    public ResponseEntity<ApiResponse<List<GanttBar>>> getAllGanttBarByGanttChartID(@PathVariable("gantt-chart-id") UUID ganttChartId){

        ApiResponse<List<GanttBar>> response = ApiResponse.<List<GanttBar>>builder()
                .success(true)
                .message("Get all gantt bar by gantt chart id successfully")
                .payload(ganttBarService.getAllGanttBarByGanttChartID(ganttChartId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{gantt-bar-id}")
    @Operation(summary = "Get gantt bar by gantt bar ID")
    public ResponseEntity<ApiResponse<GanttBar>> getGanttBarByGanttBartID(@PathVariable("gantt-bar-id") UUID ganttBarId){

        ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Get  gantt bar by gantt bar id successfully")
                .payload( ganttBarService.getGanttBarByGanttBartID(ganttBarId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


        @PutMapping("/{gantt-bar-id}/gantt-chart-")
        @Operation(summary = "Update gantt bar  by gantt bar ID")
        public ResponseEntity<ApiResponse<GanttBar>> updateGanttBarByGanttBarId(
                @PathVariable("gantt-bar-id") UUID ganttBarId,  @Valid @RequestBody GanttBarRequest ganttBarRequest){

            ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                    .success(true)
                    .message("Update gantt bar  by gantt bar id successfully")
                    .payload( ganttBarService.updateGanttBarByGanttBarId(ganttBarId, ganttBarRequest))
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(response);
        }


    @DeleteMapping("/{gantt-bar-id}")
    @Operation(summary = "Delete gantt bar  by gantt bar ID")
    public ResponseEntity<ApiResponse<GanttBar>> deleteGanttBarByGanttBarId(
            @PathVariable("gantt-bar-id") UUID ganttBarId){
        ganttBarService.deleteGanttBarByGanttBarId(ganttBarId);
        ApiResponse<GanttBar> response = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Delete gantt bar  by gantt bar id successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
