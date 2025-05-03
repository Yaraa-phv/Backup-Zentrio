package org.example.zentrio.service;

import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.model.Workspace;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    Workspace createWorkspace(WorkspaceRequest workspaceRequest);

    List<Workspace> getAllWorkspaces();

    List<Workspace> getWorkspaceByTitle(String title);

    Workspace getWorkspaceById(UUID workspaceId);

    Workspace updateWorkspaceById(UUID workspaceId, WorkspaceRequest workspaceRequest);

    Workspace updateWorkspaceTitleByWorkspaceId(UUID workspaceId, String title);

    Workspace updateWorkspaceDescriptionByWorkspaceId(UUID workspaceId, String description);

    UUID checkExistedWorkspaceId(UUID existedWorkspaceId);

    Workspace deleteWorkspaceByWorkspaceId(UUID workspaceId);
}
