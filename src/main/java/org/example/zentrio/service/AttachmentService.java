package org.example.zentrio.service;

import org.example.zentrio.dto.request.AttachmentRequest;
import org.example.zentrio.model.Attachment;

import java.util.UUID;

public interface AttachmentService {
    Attachment createAttachment(AttachmentRequest attachmentRequest, UUID checklistId);

    Attachment updateAttachment(AttachmentRequest attachmentRequest, UUID checklistId, UUID attachmentId);

    Attachment getAttachmentByChecklistId(UUID checklistId);

    Attachment deleteAttachmentById(UUID checklistId,UUID attachmentId);
}
