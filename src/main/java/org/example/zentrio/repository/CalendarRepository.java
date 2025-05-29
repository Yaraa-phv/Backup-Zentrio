package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.CalendarRequest;
import org.example.zentrio.model.Calendar;
import org.example.zentrio.model.Task;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface CalendarRepository {

    @Results(id = "TaskMapperCalender", value = {
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "status", column = "status"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "startedAt", column = "started_at"),
            @Result(property = "finishedAt", column = "finished_at"),
            @Result(property = "taskOrder", column = "task_order"),
            @Result(property = "stage", column = "stage"),
            @Result(property = "boardId", column = "board_id"),
            @Result(property = "ganttBarId", column = "gantt_bar_id"),
            @Result(property = "createdBy", column = "created_by"),
    })
    @Select("""
         
            SELECT DISTINCT ta.*
         FROM tasks ta
                  JOIN boards b ON b.board_id = ta.board_id
                  JOIN members m ON m.board_id = ta.board_id AND m.user_id = #{userId}
             JOIN roles r ON m.role_id = r.role_id
         
         -- Left joins for filtering based on role
             LEFT JOIN task_assignments tsa ON ta.task_id = tsa.task_id
             LEFT JOIN checklists ch ON ch.task_id = ta.task_id
             LEFT JOIN checklist_assignments ca ON ch.checklist_id = ca.checklist_id
         
         WHERE ta.board_id = #{boardId}
           AND (
         -- PM gets all tasks
             r.role_name = 'ROLE_MANAGER'
         
         -- Team Lead gets tasks where he is the leader (assigned_by)
            OR (r.role_name = 'ROLE_LEADER' AND tsa.assigned_to = m.member_id)
         
         -- Member gets tasks via checklist assignment
            OR (r.role_name = 'ROLE_MEMBER' AND ca.member_id = m.member_id)
             )
            """)
    List<Task> getCalendarTasksForUser(UUID userId, UUID boardId);

    @Select(""" 
            
            INSERT INTO  calendars(noted,noted_at,till_date,user_id,task_id)
              VALUES ( #{request.noted} ,#{request.startDate} ,#{request.tillDate} ,#{userId} ,#{request.taskId})
            RETURNING *
            """)
   @Results(id = "calendarMapper", value = {
           @Result(property = "calendarId", column = "calendar_id"),
           @Result(property = "noted", column = "noted"),
           @Result(property = "notedAt", column = "noted_at"),
           @Result(property = "tillDate", column = "till_date"),
           @Result(property = "userId", column = "user_id"),
           @Result(property = "taskId", column = "task_id"),
   })
    Calendar createCalendar(@Param("request") CalendarRequest calendarRequest, LocalDateTime now, UUID userId);


    @Select("""
            SELECT * FROM calendars WHERE calendar_id=#{notedId}
            """)
    @ResultMap("calendarMapper")
    Calendar getCalendarById(UUID notedId);


    @Select("""
        SELECT * FROM  calendars WHERE 
                                 task_id= #{taskId} AND 
                                 user_id= #{userId}
        """)
    @ResultMap("calendarMapper")
    List<Calendar> getAllNoteByTaskId(UUID taskId, UUID userId);



    @Select("""
            UPDATE calendars SET noted= #{request.noted},
                                 till_date= #{request.tillDate}
            WHERE calendar_id= #{calendarId} AND task_id=#{request.taskId}
            AND user_id= #{userId}
            RETURNING *
            """)
    @ResultMap("calendarMapper")
    Calendar UpdateCalendarByNoteId(@Param("request") CalendarRequest calendarRequest, UUID calendarId, UUID userId);


    @Select("""
        DELETE  FROM  calendars WHERE
                                      calendar_id= #{noteId} 
                                  AND task_id= #{taskId} 
                                  AND user_id= #{userId}
        """)
     void deleteCalendarByNoteId(UUID noteId, UUID taskId, UUID userId);

}
