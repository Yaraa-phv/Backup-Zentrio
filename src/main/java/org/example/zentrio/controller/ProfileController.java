package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ProfileRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.AppUserResponse;
import org.example.zentrio.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "Get user profile", description = "Fetches the details of the currently authenticated user.")
    @GetMapping("/preview-profile")
    public ResponseEntity<ApiResponse<AppUserResponse>> getProfile() {
        ApiResponse<AppUserResponse> apiResponse = ApiResponse.<AppUserResponse>builder()
                .success(true)
                .message("Get profile successfully")
                .payload(profileService.getProfile())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Update user profile", description = "Updates the details of the currently authenticated user. Provide the necessary fields in the request body.")
    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<AppUserResponse>> updateProfile(@Valid ProfileRequest profileRequest) {
        ApiResponse<AppUserResponse> apiResponse = ApiResponse.<AppUserResponse>builder()
                .success(true)
                .message("Updated profile successfully")
                .payload(profileService.updateProfile(profileRequest))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Delete user profile", description = "Deletes the currently authenticated user's profile. This action cannot be undone.")
    @DeleteMapping("/delete-profile")
    public ResponseEntity<ApiResponse<?>> deleteProfile() {
        ApiResponse<AppUserResponse> apiResponse = ApiResponse.<AppUserResponse>builder()
                .success(true)
                .message("Deleted profile successfully")
                .payload(null)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
