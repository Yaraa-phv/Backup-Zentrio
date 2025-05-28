package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AttachmentRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Attachment;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.repository.AttachmentRepository;
import org.example.zentrio.repository.ChecklistRepository;
import org.example.zentrio.service.AttachmentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final ChecklistRepository checklistRepository;


    private void validateAttachmentDetails(AttachmentRequest attachment) {
        Map<String, String> details = attachment.getDetails();
        if (details == null || details.isEmpty()) {
            throw new BadRequestException("Attachment details cannot be empty.");
        }

        String fileName = details.get("fileName");
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new BadRequestException("Attachment fileName is required in details.");
        }

        if (!fileName.matches(".*\\.(pdf|docx|png|jpg|jpeg|xlsx)$")) {
            throw new BadRequestException("Invalid file type for: " + fileName);
        }

        String fileSizeStr = details.get("fileSize");
        if (fileSizeStr != null) {
            try {
                long fileSize = Long.parseLong(fileSizeStr);
                if (fileSize > 10 * 1024 * 1024) {
                    throw new BadRequestException("Attachment " + fileName + " exceeds max allowed size of 10MB.");
                }
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid fileSize format in attachment " + fileName);
            }
        }
    }

    void validateChecklistAndAttachment(UUID checklistId, UUID attachmentId) {
        Checklist checklist = checklistRepository.getChecklistById(checklistId);
        if(checklist == null) {
            throw new BadRequestException("Checklist with id " + checklistId + " does not exist.");
        }
        Attachment attachment = attachmentRepository.getAttachmentById(attachmentId);
        if(attachment == null) {
            throw new BadRequestException("Attachment with id " + attachmentId + " does not exist.");
        }
    }


    // note to ask
    @Override
    public Attachment createAttachment(AttachmentRequest attachmentRequest, UUID checklistId) {
        int checklistIsAssigned = attachmentRepository.countByChecklistId(checklistId);
        if (checklistIsAssigned > 0) {
            throw new BadRequestException("Checklist with id " + checklistId + " already exists.");
        }
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Checklist checklist = checklistRepository.getChecklistById(checklistId);
        if (checklist == null) {
            throw new BadRequestException("Checklist with id " + checklistId + " not found");
        }
        validateAttachmentDetails(attachmentRequest);
        return attachmentRepository.createAttachment(attachmentRequest, checklistId,userId);
    }

    @Override
    public Attachment updateAttachment(AttachmentRequest attachmentRequest,UUID checklistId, UUID attachmentId) {
        validateChecklistAndAttachment(checklistId, attachmentId);
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
    public Attachment deleteAttachmentById(UUID checklistId,UUID attachmentId) {
        Attachment attachment = attachmentRepository.getAttachmentById(attachmentId);
        if (attachment == null) {
            throw new BadRequestException("Attachment with id " + attachmentId + " not found");
        }
        attachmentRepository.deleteAttachmentById(checklistId,attachmentId);
        return attachment;
    }

}



