package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.CalendarTaskResponse;
import org.example.zentrio.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/calendars")
@SecurityRequirement(name = "bearerAuth")
public class CalendarController {

    private final CalendarService calendarService;

    @Operation(summary = "Get calendar",description = "Get calendar with tasks for current user")
    @GetMapping
    public ResponseEntity<?> getCalendarForCurrentUser() {
        List<CalendarTaskResponse> calendarTasks = calendarService.getCalendarForCurrentUser();
        ApiResponse<List<CalendarTaskResponse>> apiResponse = ApiResponse.<List<CalendarTaskResponse>>builder()
                .success(true)
                .message("Get calendar successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
