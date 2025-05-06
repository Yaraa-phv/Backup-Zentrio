package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.model.Board;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BoardRepository {

    @Select("""
            INSERT INTO members(user_id,board_id,role_id)
            VALUES(#{userId},#{boardId},#{roleId})
            """)
    void insertMember(UUID userId, UUID boardId,UUID roleId);

    @Select("""
                INSERT INTO boards(title,description,cover,is_favourite,workspace_id)
                VALUES(#{req.title},#{req.description},#{req.cover},#{req.isFavourite}, #{workspaceId})
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
            @Result(property = "isVerified", column = "is_verified")
    })
    Board createBoard(@Param("req") BoardRequest boardRequest,UUID workspaceId);

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

    @Select("""
        UPDATE boards SET title = #{request.title},
                          description = #{request.description},
                          cover = #{request.cover},
                          is_favourite = #{request.isFavourite},
                          is_verified = #{request.isVerified}
        WHERE board_id = #{boardId}
        RETURNING *
    """)
    @ResultMap("boardMapper")
    Board updateBoardByBoardId(@Param("request") BoardRequest boardRequest, UUID boardId);


    @Select("""
        DELETE FROM boards WHERE board_id = #{boardId}
        RETURNING *
    """)
    @ResultMap("boardMapper")
    Board deleteBoardByBoardId(UUID boardId);


    @Select("""
        SELECT * FROM boards WHERE board_id = #{boardId}
    """)
    @ResultMap("boardMapper")
    Board getBoardByBoardId(UUID boardId);

    @Select("""
        SELECT * FROM boards WHERE title ILIKE '%' || #{boardTitle} || '%'
    """)
    @ResultMap("boardMapper")
    List<Board> getBoardByTitle(String boardTitle);

    @Select("""
        UPDATE boards SET title = #{boardTitle} WHERE board_id = #{boardId}
        RETURNING *
    """)
    @ResultMap("boardMapper")
    Board updateBoardTitleByBoardId(UUID boardId, String boardTitle);

    @Select("""
        SELECT * FROM boards b INNER JOIN tasks t ON b.board_id = t.board_id WHERE task_id = #{taskId}
    """)
    @ResultMap("boardMapper")
    @Result(property = "taskId", column = "task-id")
    Board getBoardByTaskId(UUID taskId);
}
