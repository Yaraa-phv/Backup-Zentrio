package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.model.GanttBar;
import org.example.zentrio.model.GanttChart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface GanttChartRepository {

    @Results(id = "ganttChartMapper", value = {
            @Result(property = "ganttChartId", column = "gantt_chart_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "boardId", column = "board_id")
    })
    @Select("""
                INSERT INTO gantt_charts (title,board_id)
                VALUES (#{request.title},#{boardId})
                RETURNING *
            """)
    GanttChart createGanttChartByBoardId(@Param("request") GanttChartRequest ganttChartRequest, UUID boardId);

    @Select("""
                SELECT * FROM gantt_charts WHERE board_id = #{boardId}
            """)
    @ResultMap("ganttChartMapper")
    GanttChart getAllGanttChartByBoardId(UUID boardId);

    @Select("""
                UPDATE gantt_charts SET title = #{request.title}
                RETURNING *
            """)
    @ResultMap("ganttChartMapper")
    GanttChart updateGanttChartByGanttChartId(@Param("request") GanttChartRequest ganttChartRequest, UUID ganttChartId);

    @Select("""
                SELECT * FROM gantt_charts WHERE gantt_chart_id = #{ganttChartId}
            """)
    @ResultMap("ganttChartMapper")
    GanttChart getGanttChartById(UUID ganttChartId);

    @Select("""
                DELETE FROM gantt_charts WHERE gantt_chart_id = #{ganttChartId}
                RETURNING *
            """)
    @ResultMap("ganttChartMapper")
    GanttChart deleteGanttChartById(UUID ganttChartId);


    @Select("""
                SELECT * FROM gantt_charts
                WHERE board_id = #{boardId}
                LIMIT 1
            """)
    @ResultMap("ganttChartMapper")
    GanttChart getGanttChartByBoardId(UUID boardId);
}
