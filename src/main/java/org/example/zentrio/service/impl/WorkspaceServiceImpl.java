package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.repository.WorkspaceRepository;
import org.example.zentrio.service.AppUserService;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final AppUserService appUserService;

    public UUID currentUserId(){
        return appUserService.getCurrentUserId();
    }

    @Override
    public UUID checkExistedWorkspaceId(UUID existedWorkspaceId) {

        UUID currentUserId = appUserService.getCurrentUserId();
        Workspace workspaceById = workspaceRepository.getWorkspaceById(existedWorkspaceId, currentUserId);
        if ( !existedWorkspaceId.equals(workspaceById.getWorkspaceId())){
            throw new NotFoundException("Workspace Id not found");
        }else {
            return existedWorkspaceId;
        }

    }

    @Override
    public Workspace createWorkspace(WorkspaceRequest workspaceRequest) {

        System.out.println("User Id in Workspace : " + currentUserId());

        Workspace create = workspaceRepository.createWorkspace(workspaceRequest, currentUserId());
        System.out.println("Create Workspace : " +create);

        return create;
    }

    @Override
    public List<Workspace> getAllWorkspaces() {

        return workspaceRepository.getAllWorkspaces(currentUserId());
    }

    @Override
    public Workspace getWorkspaceById(UUID workspaceId) {

        checkExistedWorkspaceId(workspaceId);
        return workspaceRepository.getWorkspaceById(workspaceId, currentUserId());

    }

    @Override
    public List<Workspace> getWorkspaceByTitle(String title) {

        if (title.isEmpty()){
            throw new NotFoundException("Workspace Title not found!");
        }else {
            return workspaceRepository.getWorkspaceByTitle(title, currentUserId());
        }
    }

    @Override
    public Workspace updateWorkspaceById(UUID workspaceId, WorkspaceRequest workspaceRequest) {

        checkExistedWorkspaceId(workspaceId);
        workspaceRequest.setUpdatedAt(LocalDateTime.now());
        return workspaceRepository.updateWorkspaceById(workspaceId, workspaceRequest, currentUserId());

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

        checkExistedWorkspaceId(workspaceId);
        Workspace request = workspaceRepository.getWorkspaceById(workspaceId, currentUserId());
        request.setTitle(description);
        request.setUpdatedAt(LocalDateTime.now());
        return workspaceRepository.updateWorkspaceTitleByWorkspaceId(request);

    }
}