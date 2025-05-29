package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.AttachmentRequest;
import org.example.zentrio.model.Attachment;
import org.example.zentrio.utility.JsonbTypeHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper
public interface AttachmentRepository {

    @Results(id = "attachmentMapper", value = {
            @Result(property = "attachmentId", column = "attachment_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "checklistId", column = "checklist_id"),
            @Result(property = "details", column = "details", typeHandler = JsonbTypeHandler.class)
    })

    @Select("""
                INSERT INTO attachments (details, checklist_id, created_by)
                VALUES (#{req.details, jdbcType=OTHER, typeHandler=org.example.zentrio.utility.JsonbTypeHandler},
                        #{checklistId},
                        #{userId})
                RETURNING *
            """)
    Attachment createAttachment(@Param("req") AttachmentRequest attachmentRequest, UUID checklistId, UUID userId);

    @Select("""
                UPDATE attachments SET details = #{req.details, jdbcType = OTHER, typeHandler=org.example.zentrio.utility.JsonbTypeHandler},
                                       updated_at = #{updatedAt}
                WHERE attachment_id = #{attachmentId}
                RETURNING *
            """)
    @ResultMap("attachmentMapper")
    Attachment updateAttachment(@Param("req") AttachmentRequest attachmentRequest, UUID attachmentId, LocalDateTime updatedAt);


    @Select("""
                SELECT * FROM attachments WHERE attachment_id = #{attachmentId}
            """)
    @ResultMap("attachmentMapper")
    Attachment getAttachmentById(UUID attachmentId);

    @Select("""
                SELECT * FROM attachments
                WHERE checklist_id = #{checklistId}
            """)
    @ResultMap("attachmentMapper")
    Attachment getAttachmentByChecklistId(UUID checklistId);


    @Select("""
                DELETE FROM attachments
                WHERE checklist_id = #{checklistId}
                AND attachment_id = #{attachmentId}
            """)
    void deleteAttachmentById(UUID checklistId,UUID attachmentId);


    @Select("""
            SELECT COUNT(*) FROM attachments WHERE checklist_id = #{checklistId}
    """)
    int countByChecklistId(@Param("checklistId") UUID checklistId);
}
