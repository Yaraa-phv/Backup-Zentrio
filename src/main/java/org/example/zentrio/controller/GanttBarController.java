package org.example.zentrio.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.service.GanttBarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/gantt-bars")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gantt-bars Controller")
public class GanttBarController {

    private final GanttBarService ganttBarService;

    @Operation(summary = "Create gantt bars with gantt chart ID", description = "Created gantt bars with specific gantt charts ID")
    @PostMapping("/gantt-charts")
    public ResponseEntity<ApiResponse<GanttBar>> createGanttBarByGanttChartId(
            @Valid @RequestBody GanttBarRequest ganttBarRequest) {
        ApiResponse<GanttBar> apiResponse = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Created gantt bar successfully")
                .payload(ganttBarService.createGanttBarByGanttChartId(ganttBarRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Get gantt bar by ID AND gantt chart ID", description = "Get gantt bar with ID with current gantt chart ID")
    @GetMapping("/{gantt-bar-id}/ganttCharts/{gantt-chart-id}")
    public ResponseEntity<ApiResponse<GanttBar>> getGanttBarById(
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @PathVariable("gantt-chart-id") UUID ganttChartId) {
        ApiResponse<GanttBar> apiResponse = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Created gantt bar successfully")
                .payload(ganttBarService.getGanttBarById(ganttBarId,ganttChartId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get all gantt bars by gantt charts ID", description = "Get all gantt bars by gantt charts ID")
    @GetMapping("/gantt-charts/{gantt-chart-id}")
    public ResponseEntity<ApiResponse<HashSet<GanttBar>>> getAllGanttBarsByGanttChartId(@PathVariable("gantt-chart-id") UUID ganttChartId) {
        ApiResponse<HashSet<GanttBar>> apiResponse = ApiResponse.<HashSet<GanttBar>>builder()
                .success(true)
                .message("Get all gantt bars successfully")
                .payload(ganttBarService.getAllGanttBarsByGanttChartId(ganttChartId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Update gantt bar by ID AND gantt chart ID", description = "Updated gantt bars by ID with current gantt charts")
    @PutMapping("/{gantt-bar-id}/ganttCharts/")
    public ResponseEntity<ApiResponse<GanttBar>> updateGanttBarByGanttBarId(
            @Valid @RequestBody GanttBarRequest ganttBarRequest,
            @PathVariable("gantt-bar-id") UUID ganttBarId) {
        ApiResponse<GanttBar> apiResponse = ApiResponse.<GanttBar>builder()
                .success(true)
                .message("Updated gantt bar successfully")
                .payload(ganttBarService.updateGanttBarByGanttBarId(ganttBarRequest, ganttBarId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Delete gantt bar", description = "Deleted gantt bar by ID with current gantt charts")
    @DeleteMapping("/{gantt-bar-id}/ganttCharts/{gantt-chart-id}")
    public ResponseEntity<?> deleteGanttBarByGanttBarId(
            @PathVariable("gantt-bar-id") UUID ganttBarId,
            @PathVariable("gantt-chart-id") UUID ganttChartId) {
        ganttBarService.deleteGanttBarByGanttBarId(ganttBarId,ganttChartId);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Deleted gantt bar successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get all gantt bar for current user",description = "Get all gantt bar for current users")
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<HashSet<GanttBar>>> getAllGanttBarsForCurrentUser() {
        ApiResponse<HashSet<GanttBar>> apiResponse = ApiResponse.<HashSet<GanttBar>>builder()
                .success(true)
                .message("Get all gantt bars for current users successfully")
                .payload(ganttBarService.getAllGanttBarsForCurrentUser())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get all gantt bar",description = "Get all gantt bars for all users")
    @GetMapping
    public ResponseEntity<ApiResponse<HashSet<GanttBar>>> getAllGanttBars() {
        ApiResponse<HashSet<GanttBar>> apiResponse = ApiResponse.<HashSet<GanttBar>>builder()
                .success(true)
                .message("Get all gantt bars successfully")
                .payload(ganttBarService.getAllGanttBars())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
