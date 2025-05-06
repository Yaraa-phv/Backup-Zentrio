package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.TaskRequest;
import org.example.zentrio.model.Task;

import java.util.List;
import java.util.UUID;

@Mapper
public interface TaskRepository {

    @Select("""
        INSERT INTO tasks(title, description, started_at, finished_at, stage, board_id, gantt_bar_id)
        VALUES(#{request.title}, #{request.description}, #{request.startedAt}, #{request.finishedAt}, 'TO DO', #{boardId}, #{ganttBarId})
        RETURNING *
    """)
    @Results(id = "taskMapper", value = {
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "isDone", column = "is_done"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "startedAt", column = "started_at"),
            @Result(property = "finishedAt", column = "finished_at"),
            @Result(property = "taskOrder", column = "task_order"),
            @Result(property = "stage", column = "stage"),
            @Result(property = "boardId", column = "board_id"),
            @Result(property = "ganttBarId", column = "gantt_bar_id"),
    })
    Task createTask(UUID boardId, UUID ganttBarId, @Param("request") TaskRequest taskRequest);

    @Select("""
        SELECT * FROM tasks WHERE board_id = #{boardId} AND gantt_bar_id = #{ganttBarId}
    """)
    @ResultMap("taskMapper")
    List<Task> getAllTasks(UUID boardId, UUID ganttBarId);

    @Select("""
        SELECT * FROM tasks WHERE board_id = #{boardId} AND gantt_bar_id = #{ganttBarId} AND task_id = #{taskId}
    """)
    @ResultMap("taskMapper")
    Task getTaskById(UUID boardId, UUID ganttBarId, UUID taskId);

    @Select("""
        SELECT * FROM tasks WHERE board_id = #{boardId} AND gantt_bar_id = #{ganttBarId} AND title ILIKE '%' || #{title} || '%'
    """)
    @ResultMap("taskMapper")
    List<Task> getTaskByTitle(UUID boardId, UUID ganttBarId, String title);

    @Select("""
        UPDATE tasks SET 
                         title = #{request.title},
                         description = #{request.description},
                         started_at = #{request.startedAt},
                         finished_at = #{request.finishedAt}
        WHERE task_id = #{taskId}
        RETURNING *
    """)
    @ResultMap("taskMapper")
    Task updateTaskById(UUID taskId, @Param("request") TaskRequest taskRequest);

    @Select("""
        UPDATE tasks SET title = #{title} WHERE task_id = #{taskId}
        RETURNING *
    """)
    @ResultMap("taskMapper")
    Task updateTaskTitleByTaskId(UUID taskId, String title);

    @Select("""
        UPDATE tasks SET description = #{description} WHERE task_id = #{taskId}
        RETURNING *
    """)
    @ResultMap("taskMapper")
    Task updateTaskDescriptionByTaskId(UUID taskId, String description);

    @Select("""
        DELETE FROM tasks WHERE task_id = #{taskId}
    """)
    @ResultMap("taskMapper")
    Task deleteTaskByTaskId(UUID taskId);

    @Select("""
        SELECT * FROM tasks WHERE task_id = #{taskId}
    """)
    @ResultMap("taskMapper")
    Task getTaskByTaskId(UUID taskId);

}
