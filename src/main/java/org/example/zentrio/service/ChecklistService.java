package org.example.zentrio.service;


import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.model.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashSet;
import java.util.UUID;

public interface ChecklistService {

    Checklist createChecklist(ChecklistRequest checklistRequest, UUID taskId);

    Checklist getChecklistById(UUID checklistId);

    ApiResponse<HashSet<Checklist>> getAllChecklistsByTaskId(UUID taskId, Integer page, Integer size);

    Checklist updateChecklistByIdAndTaskId(ChecklistRequest checklistRequest, UUID checklistId, UUID taskId);

    Checklist deleteChecklistByIdAndTaskId(UUID checklistId, UUID taskId);

    void assignMemberToChecklist(UUID checklistId, UUID taskId, UUID assignedBy, UUID assignedTo);

    FileMetadata uploadChecklistCoverImage(UUID checklistId, MultipartFile file);

    InputStream getFileByFileName(UUID checklistId,String fileName);

    void updateStatusOfChecklistById(UUID checklistId);


}
