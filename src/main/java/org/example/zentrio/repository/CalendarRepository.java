package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.CalendarRequest;
import org.example.zentrio.model.Calendar;
import org.example.zentrio.model.Checklist;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface CalendarRepository {


//    @Select("""
//        SELECT t.title,t.task_id FROM task_assignment
//        INNER JOIN tasks t ON task_assignment.task_id = t.task_id
//        INNER JOIN members ON task_assignment.assigned_to = members.member_id
//        INNER JOIN calendars c ON t.task_id = c.task_id
//        WHERE c.user_id = #{userId}
//    """)
//    List<Calendar> getCalendarForCurrentUser(UUID userId);


    @Results(id = "checklistMapperCalender", value = {
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
            SELECT DISTINCT *
            FROM checklists ch
                     JOIN tasks ta ON ch.task_id = ta.task_id AND ta.board_id = #{boardId}
            -- checklist assignment
                     LEFT JOIN checklist_assignments ca ON ch.checklist_id = ca.checklist_id
            -- task assignment
                     LEFT JOIN task_assignment tsa ON ta.task_id = tsa.task_id
            -- member info
                     JOIN members m ON m.user_id = #{userId} AND m.board_id = #{boardId}
            -- roles
                     JOIN roles r ON m.role_id = r.role_id
            
            WHERE
               -- checklist directly assigned
                ca.member_id = m.member_id
               -- or task involvement
               OR tsa.assigned_to = m.member_id
               OR tsa.assigned_by = m.member_id;   
          
            """)
    List<Checklist> getCalendarTasksForUser(UUID userId, UUID boardId);

    @Select(""" 
            
            INSERT INTO  calendars(noted,noted_at,till_date,user_id,checklist_id,comment_id)
              VALUES ( #{request.noted} ,#{request.startDate} ,#{request.tillDate} ,#{userId} ,#{checkListId}, #{commentId})
            RETURNING *
            """)
   @Results(id = "calendarMapper", value = {
           @Result(property = "calendarId", column = "calendar_id"),
           @Result(property = "noted", column = "noted"),
           @Result(property = "notedAt", column = "noted_at"),
           @Result(property = "tillDate", column = "till_date"),
           @Result(property = "userId", column = "user_id"),
           @Result(property = "checkListId", column = "checklist_id"),
           @Result(property = "commentId", column = "comment_id")
   })
    Calendar createCalendar(@Param("request") CalendarRequest calendarRequest, LocalDateTime now, UUID userId, UUID checkListId, UUID commentId);


    @Select("""
            SELECT * FROM calendars WHERE calendar_id=#{notedId}
            """)
    @ResultMap("calendarMapper")
    Calendar getCalendarById(UUID notedId);


    @Select("""
        SELECT * FROM  calendars WHERE checklist_id=#{checklistId}
        """)
    @ResultMap("calendarMapper")
    List<Calendar> getAllCalendarByChecklistId(UUID checklistId);



    @Select("""
            UPDATE calendars SET noted= #{request.noted},
                                 till_date= #{request.tillDate}
            WHERE calendar_id= #{calendarId}
            RETURNING *
            """)
    @ResultMap("calendarMapper")
    Calendar UpdateCalendarByNoteId(@Param("request") CalendarRequest calendarRequest, UUID calendarId);


    @Select("""
        DELETE  FROM  calendars WHERE calendar_id= #{noteId}
        RETURNING*
        """)
    @ResultMap("calendarMapper")
    Calendar deleteCalendarByNoteId(UUID noteId);

}
