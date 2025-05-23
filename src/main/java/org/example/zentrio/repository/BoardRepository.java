package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.BoardRequest;
import org.example.zentrio.dto.response.BoardResponse;
import org.example.zentrio.model.Board;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BoardRepository {

    @Select("""
            INSERT INTO members(user_id,board_id,role_id)
            VALUES(#{userId},#{boardId},#{roleId})
            """)
    void insertMember(UUID userId, UUID boardId, UUID roleId);

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
            @Result(property = "workspaceId", column = "workspace_id")
    })
    Board createBoard(@Param("req") BoardRequest boardRequest, UUID workspaceId);

    @Select("""
                SELECT * FROM boards WHERE workspace_id = #{workspaceId}
                LIMIT #{limit} OFFSET #{offset}
            """)
    @ResultMap("boardMapper")
    List<Board> getAllBoardsByWorkspaceId(UUID workspaceId, Integer limit, Integer offset);


    @Select("""
                UPDATE boards SET title = #{request.title},
                                  description = #{request.description},
                                  cover = #{request.cover},
                                  is_favourite = #{request.isFavourite}
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
                SELECT COUNT(*) FROM boards WHERE workspace_id = #{workspaceId}
            """)
    Integer getBoardCountByWorkspaceId(UUID workspaceId);


    @Select("""
                SELECT *  FROM boards WHERE board_id = #{boardId}
            """)
    @Results(id = "boardDataMapper", value = {
            @Result(property = "board", column = "board_id",
                    one = @One(select = "getBoardByBoardId")),
            @Result(property = "members", column = "board_id",
                    many = @Many(select = "org.example.zentrio.repository.MemberRepository.getMembersByBoardId"))
    })
    BoardResponse getBoardByBoardIdWithMember(UUID boardId);


    @Select("""
                SELECT member_id FROM members
                WHERE user_id = #{userId}
                AND board_id = #{boardId}
            """)
    UUID getMemberIdByUserIdAndBoardId(UUID userId, UUID boardId);


    @Select("""
                SELECT m.member_id FROM members m
                JOIN roles r ON m.role_id = r.role_id
                WHERE m.user_id = #{userId}
                  AND m.board_id = #{boardId}
                  AND r.role_name = 'ROLE_LEADER'
                LIMIT 1
            """)
    UUID getTeamLeaderMemberIdByUserIdAndBoardId(UUID userId, UUID boardId);

    @Select("""
                SELECT m.member_id FROM members m
                JOIN roles r ON m.role_id = r.role_id
                WHERE m.user_id = #{userId}
                  AND m.board_id = #{boardId}
                  AND r.role_name = 'ROLE_MANAGER'
                LIMIT 1
            """)
    UUID getManagerMemberIdByUserIdAndBoardId(UUID userId, UUID boardId);
}
