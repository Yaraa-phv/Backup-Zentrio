package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.GanttChart;
import org.example.zentrio.service.GanttChartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/gantt-chart")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gantt Chart Controller")
public class GanttChartController {

    private final GanttChartService ganttChartService;

    @Operation(summary = "Create gantt chart by board ID ")
    @PostMapping("/boards/board-id")
    public ResponseEntity<ApiResponse<GanttChart>> createGanttChart(@Valid @RequestBody GanttChartRequest ganttChartRequest){


        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Created gantt chart successfully!")
                .status(HttpStatus.CREATED)
                .payload(ganttChartService.createGanttChart( ganttChartRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Get gantt chart by board ID")
    @GetMapping("/boards/{board-id}")
    public ResponseEntity<ApiResponse<GanttChart>> getGanttChartByBoardId(@PathVariable("board-id") UUID boardId){

        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Get gantt chart successfully!")
                .status(HttpStatus.FOUND)
                .payload(ganttChartService.getGanttChartByBoardId(boardId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);}


    @Operation(summary = "Update gantt chart By gantt chart ID")
    @PutMapping("/{gantt-chart-id}/boards/{board-id}")
    public ResponseEntity<ApiResponse<GanttChart>> updateGanttChartById(@PathVariable("gantt-chart-id") UUID ganttChartId,
                                                                        @PathVariable("board-id") UUID boardId,
                                                                        @Valid @RequestBody GanttChartRequest ganttChartRequest){

        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Update gantt chart by gantt chart id successfully!")
                .status(HttpStatus.CREATED)
                .payload(ganttChartService.updateGanttChartById(ganttChartId, boardId, ganttChartRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);}



    @Operation(summary = "Delete gantt chart by gantt chart ID")
    @DeleteMapping("/{gantt-chart-id}/boards/{board-id}")
    public ResponseEntity<ApiResponse<Void>> deleteGanttChartByID(
            @PathVariable("gantt-chart-id") UUID ganttChartId,
            @PathVariable("board-id") UUID boardId
            ){

        ApiResponse<Void> response = ApiResponse.<Void> builder()
                .success(true)
                .message("Delete gantt chart by gantt chart id successfully!")
                .status(HttpStatus.ACCEPTED)
                .payload(ganttChartService.deleteGanttChartByID(ganttChartId,boardId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get gantt chart by gantt chart ID")
    @GetMapping("/{gantt-chart-id}")
    public ResponseEntity<ApiResponse<GanttChart>> getGanttChartByID(@PathVariable("gantt-chart-id") UUID ganttChartId){

        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Get gantt chart by gantt chart id successfully!")
                .status(HttpStatus.OK)
                .payload(ganttChartService.getGanttChartByID(ganttChartId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);}

}

