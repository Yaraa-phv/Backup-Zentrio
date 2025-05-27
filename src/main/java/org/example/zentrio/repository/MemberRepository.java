package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Member;


import java.util.List;
import java.util.UUID;

@Mapper
public interface MemberRepository {

    @Select("""
                SELECT m.member_id
                FROM members m
                WHERE m.user_id = #{userId}
            """)
    UUID getMemberIdByUserId(UUID userId);

    @Select("""
                INSERT INTO checklist_assignments (checklist_id,assigned_by,member_id)
                VALUES (#{checklistId},#{assignedBy},#{memberId})
            """)
    void insertIntoChecklistWithRoleMember(UUID checklistId, UUID assignedBy, UUID memberId);





    @Select("""
                SELECT DISTINCT r.role_name
                FROM members m
                JOIN roles r ON m.role_id = r.role_id
                WHERE m.user_id = #{userId}
            """)
    @Results(id = "roleMapper", value = {
            @Result(property = "roleName", column = "role_name")
    })
    List<String> getRolesByUserId(UUID userId);

    @Select("""
                SELECT username, email FROM users
                WHERE user_id = #{userId}
            """)
    AppUser getUserByUserId(UUID userId);


}