package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AnnouncementRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Announcement;
import org.example.zentrio.service.AnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/announcements")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Announcements Controller")
@RequiredArgsConstructor
public class AnnouncementController {

        private final AnnouncementService announcementService;

        @Operation(summary = "Create announcement by board id")
        @PostMapping
        public ResponseEntity<ApiResponse<Announcement>>  createAnnouncement(@Valid @RequestBody AnnouncementRequest announcementRequest) {

            ApiResponse<Announcement> apiResponse= ApiResponse.<Announcement>builder()
                    .success(true)
                    .message("Created announcement successfully")
                    .payload(announcementService.createAnnouncement(announcementRequest))
                    .status(HttpStatus.CREATED)
                    .timestamp(LocalDateTime.now())
                    .build();
            return  ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

        }

    @Operation(summary = "Get announcement by announcement id")
    @GetMapping("/{announcement-id}")
    public ResponseEntity<ApiResponse<Announcement>>  getAnnouncementById(@PathVariable("announcement-id") UUID announcementId) {

        ApiResponse<Announcement> apiResponse= ApiResponse.<Announcement>builder()
                .success(true)
                .message("Get announcement by id successfully")
                .payload(announcementService.getAnnouncementById(announcementId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Update announcement by announcement id")
    @PutMapping("{announcement-id}")
    public ResponseEntity<ApiResponse<Announcement>>  updateAnnouncementById(
            @PathVariable("announcement-id") UUID announcementId,
            @Valid @RequestBody AnnouncementRequest announcementRequest
            ) {

        ApiResponse<Announcement> apiResponse= ApiResponse.<Announcement>builder()
                .success(true)
                .message("Update announcement by id successfully")
                .payload(announcementService.updateAnnouncementById(announcementId,announcementRequest))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Update announcement pin by announcement id")
    @PutMapping("/{announcement-id}/pinned")
    public ResponseEntity<ApiResponse<Announcement>>  updateAnnouncementPinnedById(
            @PathVariable("announcement-id") UUID announcementId) {

        ApiResponse<Announcement> apiResponse= ApiResponse.<Announcement>builder()
                .success(true)
                .message("Update announcement pin by id successfully")
                .payload(announcementService.updateAnnouncementPinnedById(announcementId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Delete announcement  by announcement id")
    @DeleteMapping("/{announcement-id}")
    public ResponseEntity<ApiResponse<Void>>  deletedAnnouncementById(
            @PathVariable("announcement-id") UUID announcementId) {

        ApiResponse<Void> apiResponse= ApiResponse.<Void>builder()
                .success(true)
                .message("Delete announcement  by id successfully")
                .payload(announcementService.deletedAnnouncementById(announcementId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Get all announcement  by board id")
    @GetMapping("boards/{board-id}")
    public ResponseEntity<ApiResponse<List<Announcement>>>  getAnnouncementsByBoardId  (
            @PathVariable("board-id") UUID boardId) {

        ApiResponse<List<Announcement>> apiResponse= ApiResponse.<List<Announcement>>builder()
                .success(true)
                .message("Get all announcement  by board id successfully")
                .payload(announcementService.getAnnouncementsByBoardId(boardId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }








}
