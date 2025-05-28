package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.dto.response.MemberResponseData;
import org.example.zentrio.model.GanttBar;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Mapper
public interface GanttBarRepository {

    @Select("""
                            INSERT INTO gantt_bars(title, started_at, finished_at, gantt_chart_id)
                            VALUES (#{request.title}, #{request.startedAt}, #{request.finishedAt} , #{ganttChartId})
                            RETURNING *
            """)
    @Results(id = "ganttBarMapper", value = {
            @Result(property = "ganttBarId", column = "gantt_bar_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "startedAt", column = "started_at"),
            @Result(property = "finishedAt", column = "finished_at"),
            @Result(property = "ganttChartId", column = "gantt_chart_id"),
            @Result(property = "teamLeader", column = "gantt_bar_id",
            many = @Many(select = "teamLeadGanttBar"))

    })
    GanttBar createGanttBarByGanttChartId(@Param("request") GanttBarRequest ganttBarRequest, UUID ganttChartId);

    @Select("""
                SELECT * FROM gantt_bars
                WHERE gantt_bar_id = #{ganttBarId}
                AND gantt_chart_id = #{ganttChartId}
            """)
    @ResultMap("ganttBarMapper")
    GanttBar getGanttBarById(UUID ganttBarId, UUID ganttChartId);

    @Select("""
                SELECT * FROM gantt_bars WHERE gantt_chart_id = #{ganttChartId}
            """)
    @ResultMap("ganttBarMapper")
    HashSet<GanttBar> getAllGanttBarsByGanttChartId(UUID ganttChartId);

    @Select("""
                UPDATE gantt_bars SET title = #{request.title}, started_at = #{request.startedAt}, finished_at = #{request.finishedAt}
                WHERE gantt_bar_id = #{ganttBarId}
                AND gantt_chart_id = #{ganttChartId}
                RETURNING *
            """)
    @ResultMap("ganttBarMapper")
    GanttBar updateGanttBarByGanttBarId(@Param("request") GanttBarRequest ganttBarRequest, UUID ganttBarId, UUID ganttChartId);

    @Select("""
                DELETE FROM gantt_bars
                WHERE gantt_bar_id = #{ganttBarId}
                AND gantt_chart_id = #{ganttChartId}
                RETURNING *
            """)
    @ResultMap("ganttBarMapper")
    void deleteGanttBarByGanttBarId(UUID ganttBarId, UUID ganttChartId);

    @Select("""
                SELECT * FROM gantt_bars
                WHERE gantt_bar_id = #{ganttBarId}
            """)
    @ResultMap("ganttBarMapper")
    GanttBar getGanttBarByGanttBarId(UUID ganttBarId);

    @Select("""
               SELECT gb.*
               FROM gantt_bars gb
                INNER JOIN gantt_charts gc ON gb.gantt_chart_id = gc.gantt_chart_id
               WHERE gc.created_by = #{userId}
            """)
    @ResultMap("ganttBarMapper")
    HashSet<GanttBar> getAllGanttBarsForCurrentUser(UUID userId);

    @Select("""
        SELECT * FROM gantt_bars
    """)
    @ResultMap("ganttBarMapper")
    HashSet<GanttBar> getAllGanttBars();


    @Select("""
        
            SELECT us.username AS name,us.profile_image AS image
        FROM gantt_bars g
                 JOIN tasks ts ON ts.gantt_bar_id = g.gantt_bar_id
                 JOIN task_assignments ta ON ta.task_id = ts.task_id
                 JOIN members m ON m.member_id = ta.assigned_to
                 JOIN roles r on r.role_id= m.role_id
                 JOIN users us ON us.user_id = m.user_id
        WHERE g.gantt_bar_id = #{ganttBarId};
        """)
    @Results(id = "teamLeadGanttBarMapper", value = {
            @Result(property = "imageUrl", column = "image"),
            @Result(property = "username", column = "name"),
    })
    List<MemberResponseData> teamLeadGanttBar(UUID ganttBarId);

}
