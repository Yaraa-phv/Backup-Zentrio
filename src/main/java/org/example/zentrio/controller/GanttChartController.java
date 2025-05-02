package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.GanttChart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/gantt-chart")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gantt Chart Controller")
public class GanttChartController {

    @Operation(summary = "Create gantt chart")
    @PostMapping()
    public ResponseEntity<ApiResponse<GanttChart>> createGanttChart(@RequestBody GanttChartRequest ganttChartRequest){
        ApiResponse<GanttChart> response = ApiResponse.<GanttChart> builder()
                .success(true)
                .message("Created workspace successfully!")
                .status(HttpStatus.CREATED)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}