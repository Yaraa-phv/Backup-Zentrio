package org.example.zentrio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.dto.response.DeleteApiResponse;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Workspace Controller")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Operation(summary = "Create workspace", description = "Created workspace by current users")
    @PostMapping
    public ResponseEntity<ApiResponse<Workspace>> createWorkspace(@RequestBody @Valid WorkspaceRequest workspaceRequest) {
        ApiResponse<Workspace> apiResponse = ApiResponse.<Workspace>builder()
                .success(true)
                .message("Created workspace successfully")
                .status(HttpStatus.CREATED)
                .payload(workspaceService.createWorkspace(workspaceRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Get all workspaces", description = "Get all workspace for current users")
    @GetMapping
    public ResponseEntity<ApiResponse<HashSet<Workspace>>> getAllWorkspaces(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        ApiResponse<HashSet<Workspace>> apiResponse = workspaceService.getAllWorkspaces(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get workspace by ID", description = "Get workspace by ID with current users")
    @GetMapping("/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> getWorkspaceById(@PathVariable("workspace-id") UUID workspaceId) {
        ApiResponse<Workspace> apiResponse = ApiResponse.<Workspace>builder()
                .success(true)
                .message("Get workspace by ID successfully")
                .status(HttpStatus.OK)
                .payload(workspaceService.getWorkspaceById(workspaceId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get workspace by title", description = "Get workspace by titles")
    @GetMapping("/title/{title}")
    public ResponseEntity<ApiResponse<HashSet<Workspace>>> getWorkspaceByTitle(@PathVariable("title") String title) {
        ApiResponse<HashSet<Workspace>> apiResponse = ApiResponse.<HashSet<Workspace>>builder()
                .success(true)
                .message("Get workspace by title successfully")
                .status(HttpStatus.OK)
                .payload(workspaceService.getWorkspaceByTitle(title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Update workspace by ID", description = "Updated workspace by ID with current users")
    @PutMapping("/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> updateWorkspaceById(@PathVariable("workspace-id") UUID workspaceId, @RequestBody @Valid WorkspaceRequest workspaceRequest) {
        ApiResponse<Workspace> apiResponse = ApiResponse.<Workspace>builder()
                .success(true)
                .message("Updated workspace by ID successfully")
                .status(HttpStatus.OK)
                .payload(workspaceService.updateWorkspaceById(workspaceId, workspaceRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "Delete workspace by ID", description = "Deleted workspace by ID with current users")
    @DeleteMapping("/{workspace-id}")
    public ResponseEntity<DeleteApiResponse<Workspace>> deleteWorkspaceByWorkspaceId(@PathVariable("workspace-id") UUID workspaceId) {

        workspaceService.deleteWorkspaceByWorkspaceId(workspaceId);

        DeleteApiResponse<Workspace> apiResponse = DeleteApiResponse.<Workspace>builder()
                .success(true)
                .message("Deleted workspace by ID successfully!")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get all workspaces for users", description = "Get all workspace for display to users")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<HashSet<Workspace>>> getAllWorkspacesForAllUsers() {

        ApiResponse<HashSet<Workspace>> apiResponse = ApiResponse.<HashSet<Workspace>>builder()
                .success(true)
                .message("Get all workspaces for all users successfully")
                .payload(workspaceService.getAllWorkspacesForAllUsers())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "Get workspace by workspace ID for users", description = "Get workspace by ID to display for users")
    @GetMapping("/users/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> getWorkspaceByIdForAllUsers(@PathVariable("workspace-id") UUID workspaceId) {

        ApiResponse<Workspace> apiResponse = ApiResponse.<Workspace>builder()
                .success(true)
                .message("Get workspace by workspace ID for users successfully")
                .payload(workspaceService.getWorkspaceByIdForAllUsers(workspaceId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
