package org.example.zentrio.service;

import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Workspace;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public interface WorkspaceService {

    Workspace createWorkspace(WorkspaceRequest workspaceRequest);

    ApiResponse<HashMap<String, Workspace>> getAllWorkspaces(Integer page, Integer size);

    Set<Workspace> getWorkspaceByTitle(String title);

    Workspace getWorkspaceById(UUID workspaceId);

    Workspace updateWorkspaceById(UUID workspaceId, WorkspaceRequest workspaceRequest);

    UUID checkExistedWorkspaceId(UUID existedWorkspaceId);

    Workspace deleteWorkSpaceByWorkSpaceId(UUID workspaceId);

    Set<Workspace> getAllWorkspacesForAllUsers();

    Workspace getWorkspaceByIdForAllUsers(UUID workspaceId);

}
