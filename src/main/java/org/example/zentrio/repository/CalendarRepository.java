package org.example.zentrio.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.example.zentrio.dto.response.CalendarTaskResponse;


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


    @Results(id = "calendarMapper", value = {
            @Result(property = "calendarId", column = "calendar_id"),
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "noted", column = "noted"),
            @Result(property = "notedAt", column = "noted_at"),
            @Result(property = "tillDate", column = "till_date")
    })
    @Select("""
           
            SELECT
                t.task_id,
                t.title,
                c.calendar_id,
                c.noted_at,
                c.till_date
                FROM task_assignment ta
                JOIN tasks t ON ta.task_id = t.task_id
                JOIN calendars c ON t.task_id = c.task_id
                JOIN members m ON ta.assigned_to = m.member_id
            WHERE c.user_id = #{userId}
            """)
    List<CalendarTaskResponse> getCalendarTasksForUser(UUID userId);

}
