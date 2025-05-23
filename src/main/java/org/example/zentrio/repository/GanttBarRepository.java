package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.dto.response.MemberResponse;
import org.example.zentrio.model.GanttBar;

import java.util.List;
import java.util.UUID;

@Mapper
public interface GanttBarRepository {

    @Select(
            """
            INSERT INTO gantt_bars(title,started_at,finished_at,gantt_chart_id)
            VALUES (#{request.title},#{request.startedAt}, #{request.finishedAt}, #{ganttChartId})
            RETURNING *
            """)
    @Results(id = "ganttBarMapping", value = {
            @Result(property = "ganttBarId", column = "gantt_bar_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "startAt", column = "started_at"),
            @Result(property = "finishedAt", column = "finished_at"),
            @Result(property = "ganttChartId", column = "gantt_chart_id"),
            @Result(property = "teamLead", column = "gantt_bar_id",
            many = @Many(select = "teamLeadGanttBar")),
    })
    GanttBar createGanttBar(UUID ganttChartId,@Param("request") GanttBarRequest ganttBarRequest);



    @Select("""
    SELECT * FROM gantt_bars WHERE gantt_chart_id= #{ganttBarId}
    """)
    @ResultMap("ganttBarMapping")
    List<GanttBar> getAllGanttBarByGanttChartID(UUID ganttBarId);



    @Select(""" 
          select * from gantt_bars where gantt_bar_id= #{ganttBarId}
            """)
    @ResultMap("ganttBarMapping")
    GanttBar getGanttBarByGanttBartID(UUID ganttBarId);

    @Select("""
                UPDATE gantt_bars set title= #{request.title},
                                      started_at= #{request.startedAt},
                                      finished_at= #{request.finishedAt}
                                  WHERE gantt_bar_id= #{ganttBarId}
                RETURNING *
                
           """)
    @ResultMap("ganttBarMapping")
    GanttBar updateGanttBarByGanttBarId(UUID ganttBarId,@Param("request") GanttBarRequest ganttBarRequest);

    @Select("""
        DELETE FROM gantt_bars WHERE  gantt_bar_id= #{ganttBarId}
        """)
    void deleteGanttBarByGanttBarId(UUID ganttBarId);

    @Select("""
        SELECT * FROM gantt_bars WHERE gantt_chart_id = #{ganttChartId} AND gantt_bar_id = #{ganttBarId}
    """)
    @ResultMap("ganttBarMapping")
    GanttBar getGanttBarByGanttChartIdAndGanttBarId(UUID ganttChartId, UUID ganttBarId);

    @Select("""
        SELECT title FROM gantt_bars WHERE gantt_bar_id = #{ganttBarId}
    """)
    String getGanttBarName(UUID ganttBarId);

    @Select("""
        
            SELECT us.username AS name,us.profile_image AS image
        FROM gantt_bars g
                 JOIN tasks ts ON ts.gantt_bar_id = g.gantt_bar_id
                 JOIN task_assignment ta ON ta.task_id = ts.task_id
                 JOIN members m ON m.member_id = ta.assigned_to
                 JOIN roles r on r.role_id= m.role_id
                 JOIN users us ON us.user_id = m.user_id
        WHERE g.gantt_bar_id = #{ganttBarId};
        """)
    @Results(id = "teamLeadGanttBarMapper", value = {
            @Result(property = "imageUrl", column = "image"),
            @Result(property = "userName", column = "name"),
    })
    List<MemberResponse> teamLeadGanttBar(UUID ganttBarId);
}
