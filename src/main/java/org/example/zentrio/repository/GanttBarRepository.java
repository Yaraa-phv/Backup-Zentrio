package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.GanttBarRequest;
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
                                      started_at= #{request.startAt},
                                      finished_at= #{request.finshedAt}
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
}
