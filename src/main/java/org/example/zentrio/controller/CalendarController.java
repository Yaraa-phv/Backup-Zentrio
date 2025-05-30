package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.CalendarRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Calendar;
import org.example.zentrio.model.Task;
import org.example.zentrio.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/calendars")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Calendar Controller")
public class CalendarController {

    private final CalendarService calendarService;

    @Operation(summary = "Get calendar by board id and user id",description = "Get calendar with tasks for current user")
    @GetMapping("/boards/{board_id}")
    public ResponseEntity<ApiResponse<HashSet<Task>>> getCalendarForCurrentUser(@PathVariable("board_id") UUID board_id) {
        HashSet<Task> calendarTask = calendarService.getCalendarForCurrentUser(board_id);
        ApiResponse<HashSet<Task>> apiResponse = ApiResponse.<HashSet<Task>>builder()
                .success(true)
                .message("Get calendar by board id successfully")
                .payload(calendarTask)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Create note in calendar",description = "Create calendar with tasks for current user")
    @PostMapping("/tasks")
    public ResponseEntity<ApiResponse<Calendar>> CreateNoteInCalendar(
                                                               @Valid @RequestBody    CalendarRequest calendarRequest) {
       Calendar calendarTasks = calendarService.CreateNoteInCalendar( calendarRequest);
        ApiResponse<Calendar> apiResponse = ApiResponse.<Calendar>builder()
                .success(true)
                .message("Create note in  calendar by task id successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @Operation(summary = "Get note in calendar",description = "Get note from calendar with tasks for current user")
    @GetMapping("/{calendar-id}")
    public ResponseEntity<ApiResponse<Calendar>> getCalendarById( @PathVariable("calendar-id") UUID notedId) {
        Calendar calendarTasks = calendarService.getCalendarById(notedId);
        ApiResponse<Calendar> apiResponse = ApiResponse.<Calendar>builder()
                .success(true)
                .message("Get note by id from  calendar successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }




    @Operation(summary = "Get all note from calendar by current user",description = "Get all note from calendar ")
    @GetMapping("/tasks/{task-id}")
    public ResponseEntity<ApiResponse<HashSet<Calendar>>> getAllCalendarByTaskId(@PathVariable("task-id") UUID taskId) {
        HashSet<Calendar> calendarTasks = calendarService.getAllCalendarByTaskId(taskId);
        ApiResponse<HashSet<Calendar>> apiResponse = ApiResponse.<HashSet<Calendar>>builder()
                .success(true)
                .message("Get all note from calendar by current user successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Update note by id from calendar",description = "Update note by note id  from calendar ")
    @PutMapping("/{calendar-id}")
    public ResponseEntity<ApiResponse<Calendar>> UpdateCalendarByNoteId(@PathVariable("calendar-id") UUID noteId, @Valid @RequestBody CalendarRequest calendarRequest) {
        Calendar calendarTasks = calendarService.UpdateCalendarByNoteId(noteId, calendarRequest);
        ApiResponse<Calendar> apiResponse = ApiResponse.<Calendar>builder()
                .success(true)
                .message("Update note from calendar successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }



    @Operation(summary = "Delete note by id from calendar",description = "Delete note by note id  from calendar ")
    @DeleteMapping("/{calendar-id}/tasks/{task-id}")
    public ResponseEntity<ApiResponse<Void>> deleteCalendarByNoteId(
            @PathVariable("calendar-id") UUID noteId,
            @PathVariable("task-id")  UUID taskId) {
         calendarService.deleteCalendarByNoteId(noteId,taskId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message("Delete note from calendar successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }




}
