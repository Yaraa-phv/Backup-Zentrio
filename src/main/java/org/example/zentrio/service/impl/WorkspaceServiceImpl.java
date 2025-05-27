package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.Pagination;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.repository.WorkspaceRepository;
import org.example.zentrio.service.AuthService;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final AuthService authService;

    public UUID currentUserId() {
        return authService.getCurrentAppUserId();
    }



    @Override
    public Workspace createWorkspace(WorkspaceRequest workspaceRequest) {
        return workspaceRepository.createWorkspace(workspaceRequest, currentUserId());
    }


    @Override
    public ApiResponse<HashSet<Workspace>> getAllWorkspaces(Integer page, Integer size) {

        Integer offset = (page - 1) * size;
        UUID userId = currentUserId(); // Store once

        // Paginated list for current user
        List<Workspace> workspaceList = workspaceRepository.getAllWorkspaces(userId, size, offset);

        // Count workspaces for this specific user
        Integer totalElements = workspaceRepository.countWorkspacesByUserId(userId);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        HashSet<Workspace> workspaces = new HashSet<>(workspaceList);

        return ApiResponse.<HashSet<Workspace>>builder()
                .success(true)
                .message("Get all workspaces successfully")
                .payload(workspaces)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .pagination(new Pagination(page, totalElements, totalPages))
                .build();
    }


    @Override
    public Workspace getWorkspaceById(UUID workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId, currentUserId());
        if (workspace == null) {
            throw new NotFoundException("Workspace with ID " + workspaceId + " not found");
        }
        return workspace;
    }

    @Override
    public HashSet<Workspace> getWorkspaceByTitle(String title) {
        if (title.isEmpty()) {
            throw new NotFoundException("Workspace with " + title + " not found!");
        }
        return new HashSet<>(workspaceRepository.getWorkspaceByTitle(title));
    }

    @Override
    public Workspace updateWorkspaceById(UUID workspaceId, WorkspaceRequest workspaceRequest) {
        getWorkspaceById(workspaceId);
        return workspaceRepository.updateWorkspaceById(workspaceId, workspaceRequest, currentUserId());
    }


    @Override
    public void deleteWorkspaceByWorkspaceId(UUID workspaceId) {
        getWorkspaceById(workspaceId);
        workspaceRepository.deleteWorkspaceByWorkspaceId(workspaceId, currentUserId());
    }

    @Override
    public HashSet<Workspace> getAllWorkspacesForAllUsers() {
        return workspaceRepository.getAllWorkspacesForAllUsers();
    }

    @Override
    public Workspace getWorkspaceByIdForAllUsers(UUID workspaceId) {
        getWorkspaceById(workspaceId);
        return  workspaceRepository.getWorkspaceByWorkspaceIdForAllUsers(workspaceId);
    }
}