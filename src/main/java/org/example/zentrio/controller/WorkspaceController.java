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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Workspace Controller")
public class WorkspaceController {

    private final
    WorkspaceService workspaceService;

    @Operation(summary = "Create workspace")
    @PostMapping()
    public ResponseEntity<ApiResponse<Workspace>> createWorkspace(@RequestBody @Valid WorkspaceRequest workspaceRequest){
        ApiResponse<Workspace> response = ApiResponse.<Workspace> builder()
                .success(true)
                .message("Created workspace successfully!")
                .status(HttpStatus.CREATED)
                .payload(workspaceService.createWorkspace(workspaceRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all workspaces")
    @GetMapping()
    public ResponseEntity<ApiResponse<HashMap<String, Workspace>>> getAllWorkspaces(
             @RequestParam(defaultValue = "0") Integer page,
             @RequestParam(defaultValue = "10") Integer size
    ){
        ApiResponse<HashMap<String, Workspace>> response = workspaceService.getAllWorkspaces(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get workspace by id")
    @GetMapping("workspace-id/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> getWorkspaceById(@PathVariable("workspace-id") UUID workspaceId){
        ApiResponse<Workspace> response = ApiResponse.<Workspace> builder()
                .success(true)
                .message("Get workspace by id successfully!")
                .status(HttpStatus.OK)
                .payload(workspaceService.getWorkspaceById(workspaceId))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get workspace by title")
    @GetMapping("title/{title}")
    public ResponseEntity<ApiResponse<HashMap<String, Workspace>>> getWorkspaceByTitle(@PathVariable("title") String title){
        ApiResponse<HashMap<String, Workspace>> response = ApiResponse.<HashMap<String, Workspace>> builder()
                .success(true)
                .message("Get workspace by title successfully!")
                .status(HttpStatus.OK)
                .payload(workspaceService.getWorkspaceByTitle(title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit workspace by id")
    @PutMapping("/update/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> updateWorkspaceById(@PathVariable("workspace-id") UUID workspaceId, @RequestBody @Valid WorkspaceRequest workspaceRequest){
        ApiResponse<Workspace> response = ApiResponse.<Workspace> builder()
                .success(true)
                .message("Update workspace by id successfully!")
                .status(HttpStatus.OK)
                .payload(workspaceService.updateWorkspaceById(workspaceId, workspaceRequest))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit workspace title by workspace id")
    @PatchMapping("/updateTitle/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> updateWorkspaceTitleByWorkspaceId(@PathVariable("workspace-id") UUID workspaceId, @RequestBody String title){
        ApiResponse<Workspace> response = ApiResponse.<Workspace> builder()
                .success(true)
                .message("Update workspace title by workspace id successfully!")
                .status(HttpStatus.OK)
                .payload(workspaceService.updateWorkspaceTitleByWorkspaceId(workspaceId, title))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Edit workspace description by workspace id")
    @PatchMapping("/updateDescription/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> updateWorkspaceDescriptionByWorkspaceId(@PathVariable("workspace-id") UUID workspaceId, @RequestBody String description){
        ApiResponse<Workspace> response = ApiResponse.<Workspace> builder()
                .success(true)
                .message("Update workspace description by workspace id successfully!")
                .status(HttpStatus.OK)
                .payload(workspaceService.updateWorkspaceDescriptionByWorkspaceId(workspaceId, description))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete workspace by workspace id")
    @DeleteMapping("delete/workspace-id/{workspace-id}")
    public ResponseEntity<DeleteApiResponse<Workspace>> deleteWorkspaceByWorkspaceId(@PathVariable("workspace-id") UUID workspaceId){

        workspaceService.deleteWorkspaceByWorkspaceId(workspaceId);

        DeleteApiResponse<Workspace> response = DeleteApiResponse.<Workspace>builder()
                .success(true)
                .message("Delete workspace by workspace id successfully!")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all workspaces for all users")
    @GetMapping("/for-all-users")
    public ResponseEntity<ApiResponse<HashMap<String, Workspace>>> getAllWorkspacesForAllUsers(){

        ApiResponse<HashMap<String, Workspace>> response = ApiResponse.<HashMap<String, Workspace>>builder()
                .success(true)
                .message("Get all workspaces for all users!")
                .payload(workspaceService.getAllWorkspacesForAllUsers())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get workspace by workspace id for all users")
    @GetMapping("/for-all-users/workspace-id/{workspace-id}")
    public ResponseEntity<ApiResponse<Workspace>> getWorkspaceByIdForAllUsers(@PathVariable("workspace-id") UUID workspaceId){

        ApiResponse<Workspace> response = ApiResponse.<Workspace>builder()
                .success(true)
                .message("Get workspace by workspace id for all users!")
                .payload(workspaceService.getWorkspaceByIdForAllUsers(workspaceId))
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
