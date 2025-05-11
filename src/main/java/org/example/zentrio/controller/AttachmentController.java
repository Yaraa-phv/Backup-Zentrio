package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AttachmentRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Attachment;
import org.example.zentrio.service.AttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/attachments")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @Operation(summary = "Get attachment", description = "Get attachment by checklist id")
    @GetMapping("get-attachment/{checklist-id}")
    public ResponseEntity<ApiResponse<Attachment>> getAttachmentByChecklistId(@PathVariable("checklist-id") UUID checklistId) {
        ApiResponse<Attachment> apiResponse = ApiResponse.<Attachment>builder()
                .success(true)
                .message("Get attachment by checklist id successfully")
                .payload(attachmentService.getAttachmentByChecklistId(checklistId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Created attachment", description = "Created attachment by checklist id")
    @PostMapping("create-attachment")
    public ResponseEntity<ApiResponse<Attachment>> createAttachment(@Valid @RequestBody AttachmentRequest attachmentRequest, UUID checklistId) {
        ApiResponse<Attachment> apiResponse = ApiResponse.<Attachment>builder()
                .success(true)
                .message("Created attachment successfully")
                .payload(attachmentService.createAttachment(attachmentRequest,checklistId))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Updated attachment", description = "Updated attachment by attachment id")
    @PutMapping("updated-attachment/{attachment-id}")
    public ResponseEntity<ApiResponse<Attachment>> updateAttachment(@Valid @RequestBody AttachmentRequest attachmentRequest, @PathVariable("attachment-id") UUID attachmentId) {
        ApiResponse<Attachment> apiResponse = ApiResponse.<Attachment>builder()
                .success(true)
                .message("Updated attachment successfully")
                .payload(attachmentService.updateAttachment(attachmentRequest,attachmentId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Deleted attachment", description = "Deleted attachment by attachment id")
    @DeleteMapping("deleted-attachment/{attachment-id}")
    public ResponseEntity<ApiResponse<Attachment>> deleteAttachmentById(@PathVariable("attachment-id") UUID attachmentId) {
        ApiResponse<Attachment> apiResponse = ApiResponse.<Attachment>builder()
                .success(true)
                .message("Deleted attachment successfully")
                .payload(attachmentService.deleteAttachmentById(attachmentId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
