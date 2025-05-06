package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.Workspace;
import org.example.zentrio.repository.WorkspaceRepository;
import org.example.zentrio.service.AppUserService;
import org.example.zentrio.service.AuthService;
import org.example.zentrio.service.WorkspaceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final AppUserService appUserService;
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
        if (existedWorkspaceId.equals(workspaceId)){
            return existedWorkspaceId;
        }
        return existedWorkspaceId;

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
        if (checkExistedWorkspaceId(workspaceId) == null){
            throw new BadRequestException("Cannot access to get this workspace!");
        }
        return workspaceRepository.getWorkspaceById(workspaceId, currentUserId());

    }

    @Override
    public List<Workspace> getWorkspaceByTitle(String title) {

        if (title.isEmpty()){
            throw new NotFoundException("Workspace Title not found!");
        }else {
            return workspaceRepository.getWorkspaceByTitle(title);
        }
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

        checkExistedWorkspaceId(workspaceId);
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