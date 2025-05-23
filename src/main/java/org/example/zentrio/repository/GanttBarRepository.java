package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.GanttBarRequest;
import org.example.zentrio.model.GanttBar;

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
    })
    GanttBar createGanttBarByGanttChartId(@Param("request") GanttBarRequest ganttBarRequest, UUID ganttChartId);

    @Select("""
        SELECT * FROM gantt_bars WHERE gantt_bar_id = #{ganttBarId}
    """)
    @ResultMap("ganttBarMapper")
    GanttBar getGanttBarById(UUID ganttBarId);

    @Select("""
        SELECT * FROM gantt_bars WHERE gantt_chart_id = #{ganttChartId}
    """)
    @ResultMap("ganttBarMapper")
    List<GanttBar> getAllGanttBarsByGanttChartId(UUID ganttChartId);

    @Select("""
        UPDATE gantt_bars SET title = #{request.title}, started_at = #{request.startedAt}, finished_at = #{request.finishedAt}
        WHERE gantt_bar_id = #{ganttBarId}
        RETURNING *
    """)
    @ResultMap("ganttBarMapper")
    GanttBar updateGanttBarByGanttBarId(@Param("request") GanttBarRequest ganttBarRequest, UUID ganttBarId);

    @Select("""
        DELETE FROM gantt_bars WHERE gantt_bar_id = #{ganttBarId}
        RETURNING *
    """)
    @ResultMap("ganttBarMapper")
    GanttBar deleteGanttBarByGanttBarId(UUID ganttBarId);
}
