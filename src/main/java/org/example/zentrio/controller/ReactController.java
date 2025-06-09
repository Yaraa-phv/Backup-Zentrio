package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.dto.request.ReactRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.model.React;
import org.example.zentrio.service.ReactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

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


    @Operation(summary = "Update react by announcement id")
    @PutMapping("/{react-id}")
    public ResponseEntity<ApiResponse<React>> UpdateReact(
            @PathVariable("react-id")  UUID reactId,
            @Valid @RequestBody ReactRequest reactRequest) {
        ApiResponse<React> apiResponse = ApiResponse.<React>builder()
                .success(true)
                .message("Update React by announcement id successfully")
                .payload(reactService.UpdateReact(reactId,reactRequest))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get react by react id")
    @GetMapping("/{react-id}")
    public ResponseEntity<ApiResponse<React>> GetReactById(
            @PathVariable("react-id")  UUID reactId) {
        ApiResponse<React> apiResponse = ApiResponse.<React>builder()
                .success(true)
                .message("Get react by id successfully")
                .payload(reactService.GetReactById(reactId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Delete react by react id")
    @DeleteMapping("/{react-id}")
    public ResponseEntity<ApiResponse<Void>> DeleteReactById(
            @PathVariable("react-id")  UUID reactId) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message("Deleted react by id successfully")
                .payload(reactService.DeleteReactById(reactId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}
