package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.GanttChartRequest;
import org.example.zentrio.model.GanttChart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface  GanttChartRepository {


    @Select("""
    INSERT INTO gantt_charts(title,created_at,board_id,created_by)
    VALUES (#{request.title},#{localDateTime}, #{boardId}, #{memberId} )
    RETURNING *
    """)
    @Results(id = "ganttChartMapper", value = {
            @Result(property = "ganttChartId", column = "gantt_chart_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "createdBy", column = "created_by"),
            @Result(property = "board_id", column = "board_id")
    })
    GanttChart createGanttChart(UUID boardId, @Param("request") GanttChartRequest ganttChartRequest, LocalDateTime localDateTime, UUID memberId);

    @Select("""
    SELECT * FROM gantt_charts  WHERE board_id = #{boardId}
         """)
    @ResultMap("ganttChartMapper")
    GanttChart getGanttChartByBoardId(UUID boardId);


    @Select("""
    SELECT * FROM gantt_charts  WHERE gantt_chart_id= #{ganttChartId}
         """)
    @ResultMap("ganttChartMapper")
    GanttChart getGanttChartById(UUID ganttChartId);



    @Select("""
        
        UPDATE gantt_charts SET  title= #{request.title},
                                 updated_at = #{now}
        WHERE gantt_chart_id=#{ganttChartId}
        RETURNING *
        """)
    @ResultMap("ganttChartMapper")
    GanttChart updateGanttChartById(UUID ganttChartId,@Param("request") GanttChartRequest ganttChartRequest, LocalDateTime now);


    @Select("""
        DELETE FROM gantt_charts WHERE gantt_chart_id= #{ganttChartId}
        """)
    Void deleteGanttChartByID(UUID ganttChartId);
}
