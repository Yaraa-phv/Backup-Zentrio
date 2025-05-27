package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.WorkspaceRequest;
import org.example.zentrio.model.Workspace;


import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Mapper
public interface WorkspaceRepository {

    @Select("""
        INSERT INTO workspaces(title, description, created_by) VALUES (#{request.title}, #{request.description}, #{userId})
        RETURNING *
    """)
    @Results(id = "workspaceMapper", value = {
            @Result(property = "workspaceId", column = "workspace_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "createdBy", column = "created_by"),
    })
    Workspace createWorkspace(@Param("request") WorkspaceRequest workspaceRequest, UUID userId);

    @Select("""
        SELECT * FROM workspaces
        WHERE created_by = #{userId}
        LIMIT #{limit} OFFSET #{offset}
    """)
    @ResultMap("workspaceMapper")
    List<Workspace> getAllWorkspaces(UUID userId,Integer limit,Integer offset);

    @Select("""
        SELECT * FROM workspaces
        WHERE workspace_id = #{workspaceId}
    """)
    Workspace getWorkspaceByWorkspaceId(UUID workspaceId);

    @Select("""
         SELECT COUNT(*) FROM workspaces WHERE created_by = #{userId}
    """)
    Integer countWorkspacesByUserId(UUID userId);

    @Select("""
        SELECT * FROM workspaces WHERE workspace_id = #{workspaceId} AND created_by = #{userId}
    """)
    @ResultMap("workspaceMapper")
    Workspace getWorkspaceById(UUID workspaceId, UUID userId);

    @Select("""
        SELECT * FROM workspaces WHERE title ILIKE '%'|| #{title} ||'%'
    """)
    @ResultMap("workspaceMapper")
    List<Workspace> getWorkspaceByTitle(String title);

    @Select("""
        UPDATE workspaces SET title = #{request.title}, description = #{request.description}
        WHERE workspace_id = #{workspaceId} AND created_by = #{userId}
        RETURNING *
    """)
    @ResultMap("workspaceMapper")
    Workspace updateWorkspaceById(UUID workspaceId, @Param("request") WorkspaceRequest workspaceRequest,UUID userId);

    @Select("""
        DELETE FROM workspaces WHERE workspace_id = #{workspaceId} AND created_by = #{userId}
    """)
    @ResultMap("workspaceMapper")
    void deleteWorkspaceByWorkspaceId(UUID workspaceId, UUID userId);

    @Select("""
        SELECT * FROM workspaces WHERE workspace_id = #{workspaceId}
    """)
    @ResultMap("workspaceMapper")
    Workspace getWorkspaceByWorkspaceIdForAllUsers(UUID workspaceId);

    @Select("""
        SELECT * FROM workspaces
    """)
    @ResultMap("workspaceMapper")
    HashSet<Workspace> getAllWorkspacesForAllUsers();

}


