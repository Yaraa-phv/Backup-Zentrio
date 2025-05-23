package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.service.GanttChartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gantt-charts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gantt Chart Controller")
public class GanttChartController {

    private final GanttChartService ganttChartService;

    @Operation(summary = "Create gantt chart", description = "Created gantt chart by specific boards ID")
    @PostMapping("boards/{board-id}")
    public ResponseEntity<ApiResponse<GanttChart>> createGanttChartByBoardId(
            @Valid @RequestBody GanttChartRequest ganttChartRequest,
            @PathVariable("board-id") UUID boardId) {
        ApiResponse<GanttChart> apiResponse = ApiResponse.<GanttChart>builder()
                .success(true)
                .message("Created gantt chart successfully")
                .payload(ganttChartService.createGanttChartByBoardId(ganttChartRequest, boardId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Get gantt chart by ID", description = "Get gantt chart by ID with current boards")
    @GetMapping("/{gant-chart-id}")
    public ResponseEntity<ApiResponse<GanttChart>> getGanttChartById(@PathVariable("gant-chart-id") UUID ganttChartId) {
        ApiResponse<GanttChart> apiResponse = ApiResponse.<GanttChart>builder()
                .success(true)
                .message("Get gantt chart by ID successfully")
                .payload(ganttChartService.getGanttChartById(ganttChartId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get all gantt chart by board ID", description = "Get all gantt chart by specific boards ID")
    @GetMapping("boards/{board-id}")
    public ResponseEntity<ApiResponse<GanttChart>> getAllGanttChartByBoardId(@PathVariable("board-id") UUID boardId) {
        ApiResponse<GanttChart> apiResponse = ApiResponse.<GanttChart>builder()
                .success(true)
                .message("Get all gantt chart successfully")
                .payload(ganttChartService.getAllGanttChartByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Update gantt chart by ID", description = "Updated gantt chart by ID with current boards")
    @PutMapping("/update/{gantt-chart-id}")
    public ResponseEntity<ApiResponse<GanttChart>> updateGanttChartByGanttChartId(
            @Valid @RequestBody GanttChartRequest ganttChartRequest,
            @PathVariable("gantt-chart-id") UUID ganttChartId) {
        ApiResponse<GanttChart> apiResponse = ApiResponse.<GanttChart>builder()
                .success(true)
                .message("Updated gantt chart successfully")
                .payload(ganttChartService.updateGanttChartByGanttChartId(ganttChartRequest, ganttChartId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Delete gantt chart by ID", description = "Deleted gantt chart by ID with current boards")
    @DeleteMapping("/delete/{gantt-chart-id}")
    public ResponseEntity<ApiResponse<GanttChart>> deleteGanttChartById(@PathVariable("gantt-chart-id") UUID ganttChartId) {
        ApiResponse<GanttChart> apiResponse = ApiResponse.<GanttChart>builder()
                .success(true)
                .message("Deleted gantt chart successfully")
                .payload(ganttChartService.deleteGanttChartById(ganttChartId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}

