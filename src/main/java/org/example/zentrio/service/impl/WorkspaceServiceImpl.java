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
import java.util.*;

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
            throw new NotFoundException("You have no permission to get this workspace!");
        }
        UUID workspaceId = workspaceById.getWorkspaceId();
        if (existedWorkspaceId == null){
            throw new NotFoundException("You have no permission to get this workspace!");
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
    public ApiResponse<HashSet<Workspace>> getAllWorkspaces(Integer page, Integer size) {

        Integer offset = (page -1) * size;
        UUID userId = currentUserId(); // Store once

        HashSet<Workspace> workspaces = new HashSet<>(workspaceRepository.getAllWorkspaces(userId,size,offset));

        // Paginated list for current user
     //   List<Workspace> workspaceList = workspaceRepository.getAllWorkspaces(userId,size,offset);

        // Count workspaces for this specific user
        Integer totalElements = workspaceRepository.countWorkspacesByUserId(userId);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        System.out.println("offset" + offset);

//        for (Workspace workspace : workspaceList) {
//            workspaces.put(workspace.getWorkspaceId().toString(), workspace);
//        }

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

//        checkExistedWorkspaceId(workspaceId);
        if (checkExistedWorkspaceId(workspaceId) == null){
            throw new NotFoundException("Cannot access to get this workspace!");
        }
        return workspaceRepository.getWorkspaceById(workspaceId, currentUserId());

    }

    @Override
    public Set<Workspace> getWorkspaceByTitle(String title) {

        if (title.isEmpty()){
            throw new NotFoundException("Workspace Title not found!");
        }
        Set<Workspace> workspaces = new HashSet<>( workspaceRepository.getWorkspaceByTitle(title));
            return workspaces;
    }

    @Override
    public Workspace updateWorkspaceById(UUID workspaceId, WorkspaceRequest workspaceRequest) {

        checkExistedWorkspaceId(workspaceId);
//        workspaceRequest.setUpdatedAt(LocalDateTime.now());
        return workspaceRepository.updateWorkspaceById(workspaceId, workspaceRequest, LocalDateTime.now(), currentUserId());

    }


    @Override
    public Workspace deleteWorkSpaceByWorkSpaceId(UUID workspaceId) {

        getWorkspaceById(workspaceId);
        return workspaceRepository.deleteWorkspaceByWorkspaceId(workspaceId, currentUserId());
    }

    @Override
    public Set<Workspace> getAllWorkspacesForAllUsers() {

        Set<Workspace> workspace = new HashSet<>( workspaceRepository.getAllWorkspacesForAllUsers());
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