package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AttachmentRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.model.Attachment;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.repository.AttachmentRepository;
import org.example.zentrio.repository.ChecklistRepository;
import org.example.zentrio.service.AttachmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final ChecklistRepository checklistRepository;


    // note to ask
    @Override
    public Attachment createAttachment(AttachmentRequest attachmentRequest, UUID checklistId) {
        Checklist checklist = checklistRepository.getChecklistById(checklistId);
        if(checklist == null) {
            throw new BadRequestException("Checklist with " + checklistId + " not found");
        }
        Attachment existingAttachment = attachmentRepository.getAttachmentByChecklistId(checklistId);
        if (existingAttachment != null) {
            throw new BadRequestException("Attachment for checklist id " + checklistId +" already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        // created must be current time
        if (!attachmentRequest.getCreatedAt().isBefore(now)) {
            throw new BadRequestException("created must be in the current time.");
        }

        // updatedAt must be after now
        if (!attachmentRequest.getUpdatedAt().isAfter(now)) {
            throw new BadRequestException("updated must be after the current time.");
        }

        return attachmentRepository.createAttachment(attachmentRequest, checklistId);
    }

    @Override
    public Attachment updateAttachment(AttachmentRequest attachmentRequest, UUID attachmentId) {
        Attachment attachment = attachmentRepository.getAttachmentById(attachmentId);
        if (attachment == null) {
            throw new BadRequestException("Attachment with id " + attachmentId + " not found");
        }

        LocalDateTime now = LocalDateTime.now();
        // created must be current time
        if (!attachmentRequest.getCreatedAt().isBefore(now)) {
            throw new BadRequestException("created must be in the current time.");
        }

        // updatedAt must be after now
        if (!attachmentRequest.getUpdatedAt().isAfter(now)) {
            throw new BadRequestException("updated must be after the current time.");
        }
        return attachmentRepository.updateAttachment(attachmentRequest, attachmentId);
    }

    @Override
    public Attachment getAttachmentByChecklistId(UUID checklistId) {
        Attachment attachment = attachmentRepository.getAttachmentByChecklistId(checklistId);
        if (attachment == null) {
            throw new BadRequestException("Attachment with id " + checklistId +  " not found");
        }
        return attachment;
    }

    @Override
    public Attachment deleteAttachmentById(UUID attachmentId) {
        Attachment attachment = attachmentRepository.getAttachmentById(attachmentId);
        if (attachment == null) {
            throw new BadRequestException("Attachment with id " + attachmentId + " not found");
        }
        attachmentRepository.deleteAttachmentById(attachmentId);
        return attachment;
    }

}



