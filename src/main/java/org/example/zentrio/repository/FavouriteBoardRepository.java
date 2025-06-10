package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.FavoriteBoard;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Mapper
public interface FavouriteBoardRepository {


    @Results(id = "favoriteMapper", value = {
            @Result(property = "favoriteId", column = "favorite_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "boardId", column = "board_id"),
            @Result(property = "markedAt", column = "marked_at")
    })
    @Select("""
                INSERT INTO favorite_boards (marked_at,board_id,user_id)
                VALUES (#{markedAt},#{boardId},#{userId})
                RETURNING *
            """)
    FavoriteBoard insertFavoriteBoard(LocalDateTime markedAt, UUID boardId, UUID userId);

    @Select("""
             SELECT * FROM favorite_boards
             WHERE user_id = #{userId} AND board_id = #{boardId}
            """)
    @ResultMap("favoriteMapper")
    FavoriteBoard getByUserIdAndBoardId(UUID userId, UUID boardId);


    @Select("""
                SELECT COUNT(*) FROM favorite_boards
                WHERE board_id = #{boardId} AND user_id = #{userId}
            """)
    boolean existsByUserIdAndBoardId(UUID userId, UUID boardId);

    @Select("""
                DELETE FROM favorite_boards
                WHERE board_id = #{boardId} AND user_id = #{userId}
            """)
    void deleteFavoriteBoard(UUID boardId, UUID userId);


    @Select("""
                SELECT b.*
                FROM favorite_boards fb
                INNER JOIN public.boards b ON b.board_id = fb.board_id
                WHERE fb.user_id = #{userId}
                AND b.is_favourite = true;
            """)
    @Results(id = "boardMapper", value = {
            @Result(property = "boardId", column = "board_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "isFavourite", column = "is_favourite"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "workspaceId", column = "workspace_id")
    })
    HashSet<Board> getFavoriteBoards(UUID userId);


}
