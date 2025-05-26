package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.CalendarRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Calendar;
import org.example.zentrio.model.Checklist;
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
public class CalendarController {

    private final CalendarService calendarService;

    @Operation(summary = "Get calendar",description = "Get calendar with tasks for current user")
    @GetMapping("/{board_id}")
    public ResponseEntity<ApiResponse<HashSet<Checklist>>> getCalendarForCurrentUser( @PathVariable("board_id") UUID board_id) {
        HashSet<Checklist> calendarTasks = calendarService.getCalendarForCurrentUser(board_id);
        ApiResponse<HashSet<Checklist
                >> apiResponse = ApiResponse.<HashSet<Checklist>>builder()
                .success(true)
                .message("Get calendar successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Create calendar",description = "Create calendar with tasks for current user")
    @PostMapping("/create_note/{check_listId}")
    public ResponseEntity<ApiResponse<Calendar>> CreateNoteInCalendar( @PathVariable("check_listId") UUID check_listId,
                                                               @Valid @RequestBody    CalendarRequest calendarRequest) {
       Calendar calendarTasks = calendarService.CreateNoteInCalendar(check_listId, calendarRequest);
        ApiResponse<Calendar> apiResponse = ApiResponse.<Calendar>builder()
                .success(true)
                .message("Create note in  calendar successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Get note calendar",description = "Get note from calendar with tasks for current user")
    @GetMapping("/get_note_by_noteId/{notedId}")
    public ResponseEntity<ApiResponse<Calendar>> getCalendarById( @PathVariable("notedId") UUID notedId) {
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




    @Operation(summary = "Get all note from calendar",description = "Get all note from calendar ")
    @GetMapping("/get_all_note/{checklistId}")
    public ResponseEntity<ApiResponse<HashSet<Calendar>>> getAllCalendarByChecklistId( @PathVariable("checklistId") UUID checklistId) {
        HashSet<Calendar> calendarTasks = calendarService.getAllCalendarByChecklistId(checklistId);
        ApiResponse<HashSet<Calendar>> apiResponse = ApiResponse.<HashSet<Calendar>>builder()
                .success(true)
                .message("Get all note from calendar successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "update note by id from calendar",description = "Update note by note id  from calendar ")
    @PutMapping("/update_note/{noteId}")
    public ResponseEntity<ApiResponse<Calendar>> UpdateCalendarByNoteId(@PathVariable("noteId") UUID noteId, @Valid @RequestBody CalendarRequest calendarRequest) {
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



    @Operation(summary = "delete note by id from calendar",description = "Delete note by note id  from calendar ")
    @DeleteMapping("/delete_note/{noteId}")
    public ResponseEntity<ApiResponse<Calendar>> deleteCalendarByNoteId( @PathVariable("noteId") UUID noteId) {
        Calendar calendarTasks = calendarService.deleteCalendarByNoteId(noteId);
        ApiResponse<Calendar> apiResponse = ApiResponse.<Calendar>builder()
                .success(true)
                .message("Update note from calendar successfully")
                .payload(calendarTasks)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }




}
