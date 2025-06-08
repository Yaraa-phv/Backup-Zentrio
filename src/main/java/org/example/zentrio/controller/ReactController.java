package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.dto.request.ReactRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.model.React;
import org.example.zentrio.service.ReactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reacts")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reacts  Controller")
@RequiredArgsConstructor
public class ReactController {
    private final ReactService reactService;

    @Operation(summary = "Create react by announcement id")
    @PostMapping
    public ResponseEntity<ApiResponse<React>> createReact(
            @Valid @RequestBody ReactRequest reactRequest) {
        ApiResponse<React> apiResponse = ApiResponse.<React>builder()
                .success(true)
                .message("Created React by announcement id successfully")
                .payload(reactService.createReact(reactRequest))
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
