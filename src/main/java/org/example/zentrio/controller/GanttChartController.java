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

    @Operation(summary = "Create gantt chart")
    @PostMapping("/{boardId}")
    public ResponseEntity<ApiResponse<GanttChart>> createGanttChart(@PathVariable("boardId") UUID boardId, @Valid @RequestBody GanttChartRequest ganttChartRequest){


        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Created GanttChart successfully!")
                .status(HttpStatus.CREATED)
                .payload(ganttChartService.createGanttChart(boardId, ganttChartRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get All GanttChart By BoardID")
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<GanttChart>> getGanttChartByBoardId(@PathVariable("boardId") UUID boardId){

        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Get All GanttChart successfully!")
                .status(HttpStatus.FOUND)
                .payload(ganttChartService.getGanttChartByBoardId(boardId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);}


    @Operation(summary = "Update GanttChart By GanttChartID")
    @PutMapping("/{ganttChartID}")
    public ResponseEntity<ApiResponse<GanttChart>> updateGannntCjhartById(@PathVariable("ganttChartID") UUID ganttChartId,@Valid @RequestBody GanttChartRequest ganttChartRequest){

        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Update GanttChart BY GanttChartID successfully!")
                .status(HttpStatus.CREATED)
                .payload(ganttChartService.updateGannntCjhartById(ganttChartId, ganttChartRequest))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);}



    @Operation(summary = "Delete GanttChart By GanttChartID")
    @DeleteMapping("/{ganttChartID}")
    public ResponseEntity<ApiResponse<Void>> deleteGanttChartByID(@PathVariable("ganttChartID") UUID ganttChartId){

        ApiResponse<Void> response = ApiResponse.<Void> builder()
                .success(true)
                .message("Delete GanttChart BY GanttChartID successfully!")
                .status(HttpStatus.ACCEPTED)
                .payload(ganttChartService.deleteGanttChartByID(ganttChartId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get GanttChart By GanttChartID")
    @GetMapping("/gantt-chartid/{ganttChartID}")
    public ResponseEntity<ApiResponse<GanttChart>> getGanttChartByID(@PathVariable("ganttChartID") UUID ganttChartId){

        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Get GanttChart BY GanttChartID successfully!")
                .status(HttpStatus.FOUND)
                .payload(ganttChartService.getGanttChartByID(ganttChartId))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);}

}

