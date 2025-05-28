package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.AppUserResponse;
import org.example.zentrio.dto.response.ChecklistResponse;
import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.Report;
import org.example.zentrio.service.ReportService;
import org.example.zentrio.service.impl.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/v1/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Report Controller")
public class ReportController {
    private final ReportService reportService;
    private final PdfService   pdfService;

    @GetMapping("/boards/{board-id}")
    @Operation(summary = "Get report by board id")
    public ResponseEntity<ApiResponse<Report>> getReportByBoardId(@PathVariable("board-id") UUID boardId) {
        ApiResponse<Report> apiResponse = ApiResponse.<Report>builder()
                .success(true)
                .message("Get profile successfully")
                .payload(reportService.getReportByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

//    @GetMapping("all-user-and-role")
//    public ResponseEntity<ApiResponse <List<AllMember>>> getMember(@RequestParam UUID boardId) {
//        ApiResponse<List<AllMember>> apiResponse = ApiResponse.< List<AllMember>>builder()
//                .success(true)
//                .message("Get profile successfully")
//                .payload(reportService.getMember(boardId))
//                .status(HttpStatus.OK)
//                .timestamp(LocalDateTime.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
//
//
//    @GetMapping("get-attachment-by-checklist-Id")
//    public ResponseEntity<ApiResponse <Map<String, Object>>>  getAttachment (@RequestParam UUID checklistId) {
//        System.out.println(reportService.getAttachment(checklistId));
//
//        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.<Map<String, Object>>builder()
//                .success(true)
//                .message("Get profile successfully")
//                .payload(reportService.getAttachment(checklistId))
//                .status(HttpStatus.OK)
//                .timestamp(LocalDateTime.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
//
//
//    @GetMapping("/String")
//    public Map<String, Object>  get (@RequestParam UUID checklistId) {
//        return reportService.getAttachment(checklistId);
//
////        ApiResponse<Map<String, String>> apiResponse = ApiResponse.<Map<String, String>>builder()
////                .success(true)
////                .message("Get profile successfully")
////                .payload(reportService.getAttachment(checklistId))
////                .status(HttpStatus.OK)
////                .timestamp(LocalDateTime.now())
////                .build();
////        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
//
//
//
//
//
//    @GetMapping("get-checklist-by-Task-Id")
//    public ResponseEntity<ApiResponse <Set<ChecklistResponse>>>  getChecklistById (@RequestParam UUID checklistId) {
//        ApiResponse<Set<ChecklistResponse>> apiResponse = ApiResponse.<Set<ChecklistResponse>>builder()
//                .success(true)
//                .message("Get profile successfully")
//                .payload(reportService.getChecklistById(checklistId))
//                .status(HttpStatus.OK)
//                .timestamp(LocalDateTime.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }

@GetMapping("/pdf/boards/{board-id}")
@Operation(summary = "Generate report as pdf file by board id")
public ResponseEntity<byte[]> generateProjectReport(@PathVariable("board-id") UUID boardId) {
    Report report = reportService.getReportByBoardId(boardId); // You parse JSON into this DTO

    Map<String, Object> data = new HashMap<>();
    data.put("boardName", report.getBoardName());
    data.put("creationDate", report.getCreationDate());
    data.put("version", report.getVersion());
    data.put("allMembers", report.getAllMembers());
    data.put("allTasks", report.getAllTasks());

    byte[] pdf = pdfService.generatePdf("project-report", data); // Use your pdfService like before

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", report.getBoardName() + "-report.pdf");

    return ResponseEntity.ok()
            .headers(headers)
            .body(pdf);
}


}
