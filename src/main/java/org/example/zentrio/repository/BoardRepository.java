package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.model.Board;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BoardRepository {

    @Select("""
        INSERT INTO boards(title, description, cover, workspace_id, is_verified) 
        VALUES (#{request.title}, #{request.description}, #{request.cover}, #{request.workspaceId}, #{request.isVerified})
        RETURNING *
    """)
    @Results(id = "boardMapper", value = {
            @Result(property = "boardId", column = "board_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "cover", column = "cover"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "isFavourite", column = "is_favourite"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "workspaceId", column = "workspace_id"),
    })
    Board createBoard(@Param("request") BoardRequest boardRequest);

    @Select("""
        SELECT * FROM boards WHERE workspace_id = #{workspaceId}
    """)
    @ResultMap("boardMapper")
    List<Board> getAllBoardsByWorkspaceId(UUID workspaceId);

    @Select("""
        SELECT * FROM boards WHERE workspace_id = #{workspaceId} AND board_id = #{boardId}
    """)
    @ResultMap("boardMapper")
    Board getBoardByWorkspaceIdAndBoardId(UUID workspaceId, UUID boardId);
}
