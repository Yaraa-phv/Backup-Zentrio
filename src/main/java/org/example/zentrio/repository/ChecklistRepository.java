package org.example.zentrio.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.model.Task;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Mapper
public interface ChecklistRepository {


    @Results(id = "checklistMapper", value = {
            @Result(property = "checklistId", column = "checklist_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "checklistOrder", column = "checklist_order"),
            @Result(property = "startedAt", column = "started_at"),
            @Result(property = "finishedAt", column = "finished_at"),
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "createdBy", column = "created_by")
    })
    @Select("""
                INSERT INTO checklists(title, started_at, finished_at, task_id, created_by)
                VALUES (#{checklistRequest.title}, #{checklistRequest.startedAt}, #{checklistRequest.finishedAt},
                        #{taskId},
                        #{createdBy})
                RETURNING *
            """)
    Checklist createChecklist(@Param("checklistRequest") ChecklistRequest checklistRequest, UUID taskId, UUID createdBy);


    @Select("""
                SELECT * FROM checklists WHERE checklist_id = #{checklistId}
            """)
    @ResultMap("checklistMapper")
    Checklist getChecklistById(UUID checklistId);


    @Select("""
                SELECT COUNT(*) FROM checklists WHERE task_id = #{taskId}
            """)
    Integer countAllChecklistByTaskId(UUID taskId);


    @Select("""
                SELECT * FROM checklists WHERE task_id = #{taskId}
                LIMIT #{limit} OFFSET #{offset}
            """)
    @ResultMap("checklistMapper")
    HashSet<Checklist> getAllChecklistsByTaskId(UUID taskId, Integer limit, Integer offset);


    @Select("""
                UPDATE checklists SET title = #{checklistRequest.title},
                                      started_at = #{checklistRequest.startedAt},
                                      finished_at = #{checklistRequest.finishedAt}
                WHERE checklist_id = #{checklistId} AND task_id = #{taskId}
                RETURNING *
            """)
    @ResultMap("checklistMapper")
    Checklist updateChecklistByIdAndTaskId(@Param("checklistRequest") ChecklistRequest checklistRequest, UUID checklistId, UUID taskId);

    @Select("""
                DELETE FROM checklists WHERE checklist_id = #{checklistId} AND task_id = #{taskId}
                RETURNING *
            """)
    @ResultMap("checklistMapper")
    Checklist deleteChecklistByIdAndTaskId(UUID checklistId, UUID taskId);


    @Select("""
                SELECT m.member_id FROM members m
                WHERE m.board_id = #{boardId}
                AND m.user_id = #{assignedTo}
            """)
    UUID findMemberIdByBoardIdAndUserId(UUID boardId, UUID assignedTo);


    @Select(""" 
                    INSERT INTO checklist_assignments (checklist_id,assigned_by, member_id)
                    VALUES (#{checklistId}, #{assignerId}, #{assigneeId})
            """)
    void insertToChecklistAssignment(UUID checklistId, UUID assignerId, UUID assigneeId);


    @Select("""
                    SELECT COUNT(*) > 0
                    FROM checklist_assignments
                    WHERE checklist_id = #{checklistId}
                    AND member_id = #{assigneeId}
            """)
    boolean checklistIsAssigned(UUID checklistId, UUID assigneeId);


//    Checklist createChecklist(UUID taskId, @Param("request") ChecklistRequest checklistRequest);
//
//    Checklist getChecklistByTaskId(UUID taskId);
//
//    @Select("""
//        SELECT * FROM checklists WHERE task_id = #{taskId}
//    """)
//    @ResultMap("checklistMapper")
//    List<Checklist> getAllChecklistByTaskId(UUID taskId);
//
//    @Select("""
//        SELECT * FROM checklists WHERE task_id = #{taskId} AND checklist_id = #{checklistId}
//    """)
//    @ResultMap("checklistMapper")
//    Checklist getChecklistByTaskIdAndChecklistId(UUID taskId, UUID checklistId);
//
//    @Select("""
//        SELECT * FROM checklists WHERE task_id = #{taskId} AND title ILIKE '%' || #{title} || '%'
//    """)
//    @ResultMap("checklistMapper")
//    List<Checklist> getChecklistByTaskIdAndTitle(UUID taskId, String title);
//
//    @Select("""
//        UPDATE checklists SET
//                              title = #{request.title},
//                              started_at = #{request.startedAt},
//                              finished_at = #{request.finishedAt}
//        WHERE task_id = #{taskId} AND checklist_id = #{checklistId}
//        RETURNING *
//    """)
//    @ResultMap("checklistMapper")
//    Checklist updateChecklistById(UUID taskId, UUID checklistId, @Param("request") ChecklistRequest checklistRequest);
//
//    @Select("""
//        DELETE FROM checklists WHERE task_id = #{taskId} AND checklist_id = #{checklistId}
//    """)
//    @ResultMap("checklistMapper")
//    Checklist deleteChecklistByTaskIdAndChecklist(UUID taskId, UUID checklistId);
//
//
//
//    @Select("""
//                SELECT COUNT(*) FROM checklist_assignments
//                WHERE checklist_id = #{checklistId}
//                AND   member_id = #{memberId}
//            """)
//    boolean isExistByUserIdAndTaskId(UUID checklistId,UUID memberId);
//
//
//
//    // get checklist by checklist id
//    @Select("""
//        SELECT * FROM checklists WHERE checklist_id = #{checklistId}
//    """)
//    Checklist getChecklistById(UUID checklistId);


}