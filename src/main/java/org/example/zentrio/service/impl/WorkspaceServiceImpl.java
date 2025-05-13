package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.Pagination;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.repository.WorkspaceRepository;
import org.example.zentrio.service.AppUserService;
import org.example.zentrio.service.AuthService;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final AuthService authService;

    public UUID currentUserId(){
        //System.out.println(appUserService.getCurrentUserId());
        return authService.getCurrentAppUserId();
    }

    @Override
    public UUID checkExistedWorkspaceId(UUID existedWorkspaceId) {

        UUID currentUserId = currentUserId();
        System.out.println(currentUserId);
        Workspace workspaceById = workspaceRepository.getWorkspaceById(existedWorkspaceId, currentUserId);
        if (workspaceById == null){
            throw new BadRequestException("You have no permission to get this workspace!");
        }
        UUID workspaceId = workspaceById.getWorkspaceId();
        if (existedWorkspaceId == null){
            throw new BadRequestException("You have no permission to get this workspace!");
        }
        if (!existedWorkspaceId.equals(workspaceId)){
            throw new NotFoundException("Workspace Id not found");
        }
        return existedWorkspaceId;

    }


    @Override
    public Workspace createWorkspace(WorkspaceRequest workspaceRequest) {
        return workspaceRepository.createWorkspace(workspaceRequest, currentUserId());
    }



    @Override
    public ApiResponse<HashMap<String, Workspace>> getAllWorkspaces(Integer page, Integer size) {

        Integer offset = page * size;
        UUID userId = currentUserId(); // Store once

        HashMap<String, Workspace> workspaces = new HashMap<>();

        // Paginated list for current user
        List<Workspace> workspaceList = workspaceRepository.getAllWorkspaces(userId,size,offset);

        // Count workspaces for this specific user
        Integer totalElements = workspaceRepository.countWorkspacesByUserId(userId);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        System.out.println("offset" + offset);

        for (Workspace workspace : workspaceList) {
            workspaces.put(workspace.getWorkspaceId().toString(), workspace);
        }

        return ApiResponse.<HashMap<String, Workspace>>builder()
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

//        checkExistedWorkspaceId(workspaceId);
        if (checkExistedWorkspaceId(workspaceId) == null){
            throw new BadRequestException("Cannot access to get this workspace!");
        }
        return workspaceRepository.getWorkspaceById(workspaceId, currentUserId());

    }

    @Override
    public HashMap<String, Workspace> getWorkspaceByTitle(String title) {

        if (title.isEmpty()){
            throw new NotFoundException("Workspace Title not found!");
        }
        HashMap<String, Workspace> workspaces = new HashMap<>();
        for (Workspace w : workspaceRepository.getWorkspaceByTitle(title)){
            workspaces.put(w.getTitle(), w);
        }
            return workspaces;
    }

    @Override
    public Workspace updateWorkspaceById(UUID workspaceId, WorkspaceRequest workspaceRequest) {

        checkExistedWorkspaceId(workspaceId);
//        workspaceRequest.setUpdatedAt(LocalDateTime.now());
        return workspaceRepository.updateWorkspaceById(workspaceId, workspaceRequest, LocalDateTime.now(), currentUserId());

    }

    @Override
    public Workspace updateWorkspaceTitleByWorkspaceId(UUID workspaceId, String title) {

        checkExistedWorkspaceId(workspaceId);
        Workspace request = workspaceRepository.getWorkspaceById(workspaceId, currentUserId());
        request.setTitle(title);
        request.setUpdatedAt(LocalDateTime.now());

        return workspaceRepository.updateWorkspaceTitleByWorkspaceId(request);

    }

    @Override
    public Workspace updateWorkspaceDescriptionByWorkspaceId(UUID workspaceId, String description) {

        getWorkspaceById(workspaceId);
        Workspace request = workspaceRepository.getWorkspaceById(workspaceId, currentUserId());
        request.setTitle(description);
        request.setUpdatedAt(LocalDateTime.now());

        return workspaceRepository.updateWorkspaceTitleByWorkspaceId(request);

    }

    @Override
    public Workspace deleteWorkspaceByWorkspaceId(UUID workspaceId) {

        checkExistedWorkspaceId(workspaceId);
        return workspaceRepository.deleteWorkspaceByWorkspaceId(workspaceId, currentUserId());
    }

    @Override
    public HashMap<String, Workspace> getAllWorkspacesForAllUsers() {

        HashMap<String, Workspace> workspace = new HashMap<>();
        for (Workspace w : workspaceRepository.getAllWorkspacesForAllUsers()){
            workspace.put(w.getWorkspaceId().toString(), w);
        }
        return workspace;
    }

    @Override
    public Workspace getWorkspaceByIdForAllUsers(UUID workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceByWorkspaceIdForAllUsers(workspaceId);
        if (workspace ==null){
            throw new NotFoundException("Workspace not found");
        }
        return workspace;
    }
}