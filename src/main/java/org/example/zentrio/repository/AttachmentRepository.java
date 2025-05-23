package org.example.zentrio.repository;

import org.apache.ibatis.annotations.*;
import org.example.zentrio.dto.request.AttachmentRequest;
import org.example.zentrio.model.Attachment;
import org.example.zentrio.utility.JsonbTypeHandler;

import java.util.UUID;

@Mapper
public interface AttachmentRepository {

    @Results(id = "attachmentMapper", value = {
            @Result(property = "attachmentId",column = "attachment_id"),
            @Result(property = "createdAt",column = "created_at"),
            @Result(property = "updatedAt",column = "updated_at"),
            @Result(property = "checklistId",column = "checklist_id"),
            @Result(property = "details",column = "details",typeHandler = JsonbTypeHandler.class)
    })

    @Select("""
        INSERT INTO attachments(created_at, updated_at, details,checklist_id)
        VALUES (#{req.createdAt}, #{req.updatedAt}, #{req.details, jdbcType=OTHER, typeHandler=org.example.zentrio.utility.JsonbTypeHandler},
                #{checklistId})
        RETURNING *
    """)
    Attachment createAttachment(@Param("req") AttachmentRequest attachmentRequest, UUID checklistId);

    @Select("""
        UPDATE attachments SET details = #{req.details, jdbcType = OTHER, typeHandler=org.example.zentrio.utility.JsonbTypeHandler}
        WHERE attachment_id = #{attachmentId}
        RETURNING *
    """)
    Attachment updateAttachment(@Param("req") AttachmentRequest attachmentRequest, UUID attachmentId);


    @Select("""
        SELECT * FROM attachments WHERE attachment_id = #{attachmentId}
    """)
    @ResultMap("attachmentMapper")
    Attachment getAttachmentById(UUID attachmentId);

    @Select("""
        SELECT * FROM attachments WHERE checklist_id = #{checklistId}
    """)
    @ResultMap("attachmentMapper")
    Attachment getAttachmentByChecklistId(UUID checklistId);


    @Select("""
        DELETE FROM attachments WHERE attachment_id = #{attachmentId}
    """)
    void deleteAttachmentById(UUID attachmentId);
}
