package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AchievementRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Achievement;
import org.example.zentrio.service.AchievementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/achievements")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;


    @Operation(summary = "Get achievement", description = "Get achievement for a user")
    @GetMapping
    ResponseEntity<ApiResponse<Achievement>> getAllAchievementsByCurrentUser() {
        ApiResponse<Achievement> apiResponse = ApiResponse.<Achievement>builder()
                .success(true)
                .message("Get achievement successfully")
                .payload(achievementService.getAllAchievementByCurrentUser())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Set up achievement", description = "set up achievement for a user")
    @PostMapping
    public ResponseEntity<ApiResponse<Achievement>> createAchievement(@Valid @RequestBody AchievementRequest achievementRequest) {
        ApiResponse<Achievement> apiResponse = ApiResponse.<Achievement>builder()
                .success(true)
                .message("Create achievement successfully")
                .payload(achievementService.createAchievement(achievementRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Updated achievement", description = "update achievement for a user")
    @PutMapping
    public ResponseEntity<ApiResponse<Achievement>> updateAchievement(@Valid @RequestBody AchievementRequest achievementRequest) {
        System.out.println("Details: " + achievementRequest.getDetails());
        ApiResponse<Achievement> apiResponse = ApiResponse.<Achievement>builder()
                .success(true)
                .message("Update achievement successfully")
                .payload(achievementService.updateAchievement(achievementRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
