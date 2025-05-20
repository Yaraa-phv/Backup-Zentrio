package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.AppUserResponse;
import org.example.zentrio.dto.response.ChecklistResponse;
import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.Report;
import org.example.zentrio.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Report Controller")
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ApiResponse<Report>> getReportByBoardId(@RequestParam UUID boardId) {
        ApiResponse<Report> apiResponse = ApiResponse.<Report>builder()
                .success(true)
                .message("Get profile successfully")
                .payload(reportService.getReportByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("all-user-and-role")
    public ResponseEntity<ApiResponse <List<AllMember>>> getMember(@RequestParam UUID boardId) {
        ApiResponse<List<AllMember>> apiResponse = ApiResponse.< List<AllMember>>builder()
                .success(true)
                .message("Get profile successfully")
                .payload(reportService.getMember(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @GetMapping("get-attachment-by-checklist-Id")
    public ResponseEntity<ApiResponse <Map<String, String>>>  getAttachment (@RequestParam UUID checklistId) {
        ApiResponse<Map<String, String>> apiResponse = ApiResponse.<Map<String, String>>builder()
                .success(true)
                .message("Get profile successfully")
                .payload(reportService.getAttachment(checklistId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }



    @GetMapping("get-checklist-by-Id")
    public ResponseEntity<ApiResponse <Set<ChecklistResponse>>>  getChecklistById (@RequestParam UUID checklistId) {
        ApiResponse<Set<ChecklistResponse>> apiResponse = ApiResponse.<Set<ChecklistResponse>>builder()
                .success(true)
                .message("Get profile successfully")
                .payload(reportService.getChecklistById(checklistId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
