package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.model.AllMember;
import org.example.zentrio.model.AllTasks;
import org.example.zentrio.model.Report;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ReportRepository {

    @Select("""
    SELECT r.role_name AS role_name, COUNT(*) AS total
    FROM roles r
    INNER JOIN members m ON r.role_id = m.role_id
    INNER JOIN users u ON u.user_id = m.user_id
    WHERE m.board_id = #{boardId}
    GROUP BY r.role_name
""")
    @Results(id = "roleCountMapper", value = {
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "total", column = "total")
    })
    List<AllMember> getMember(UUID boardId);


    @Select("""
    
            SELECT stage AS tasks, COUNT(*) AS total
    FROM tasks WHERE board_id=#{boardId}
    GROUP BY tasks
    """)
    @Results(id = "tasksCountMapper", value = {
            @Result(property = "task", column = "tasks"),
            @Result(property = "total", column = "total")
    })
    List<AllTasks> getTasks(UUID boardId);



    @Select("""
        SELECT * FROM  report WHERE board_id=#{boardId}
        """)
    @Results(id = "reportMapping", value = {
            @Result(property = "reportId" , column = "report_id"),
            @Result(property = "creationDate" , column = "created_at"),
            @Result(property = "UpdateAt" , column = "updated_at"),
            @Result(property = "boardId" , column = "board_id"),
            @Result(property = "allMembers" , column = "board_id",
            many =@Many (select = "getMember") ),
            @Result(property = "boardName" , column = "board_id",
            one = @One(select = "boardName")),
            @Result(property = "allTasks" , column = "board_id",
                    many= @Many(select = "getTasks")),
    })
    Report getReportByBoardId(UUID boardId);

    @Select("""
        SELECT title FROM boards WHERE board_id= #{boardId}
        """)
    String boardName(UUID boardId);

//    SELECT r.role_name, COUNT(*) AS total
//    FROM roles r
//    INNER JOIN members m ON r.role_id = m.role_id
//    INNER JOIN users u ON u.user_id = m.user_id
//    WHERE m.board_id = '7c9d997b-1d97-4620-86a4-902cca25e286'
//    GROUP BY r.role_name

}
