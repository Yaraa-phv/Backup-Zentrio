package org.example.zentrio.service;

import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.dto.response.ApiResponse;
import org.example.zentrio.model.Checklist;

import java.util.HashSet;
import java.util.UUID;

public interface ChecklistService {
    Checklist createChecklist(ChecklistRequest checklistRequest, UUID taskId);

    Checklist getChecklistChecklistId(UUID checklistId);

    ApiResponse<HashSet<Checklist>> getAllChecklistsByTaskId(UUID taskId, Integer page, Integer size);

    Checklist updateChecklistByIdAndTaskId(ChecklistRequest checklistRequest, UUID checklistId, UUID taskId);

    Checklist deleteChecklistByIdAndTaskId(UUID checklistId, UUID taskId);

    void assignMemberToChecklist(UUID checklistId, UUID taskId, UUID assignedBy, UUID assignedTo);


//    Checklist createChecklist(UUID taskId, ChecklistRequest checklistRequest);

//    UUID checkExistedTaskId(UUID taskId, UUID currentUserId);
//
//    UUID checkTaskIdToGetChecklist(UUID taskId);
//
//    HashMap<String, Checklist> getAllChecklistByTaskId(UUID taskId);
//
//    Checklist getChecklistByTaskIdAndChecklistId(UUID taskId, UUID checklistId);
//
//    HashMap<String, Checklist> getChecklistByTaskIdAndTitle(UUID taskId, String title);
//
//    Checklist updateChecklistById(UUID taskId, UUID checklistId, ChecklistRequest checklistRequest);
//
//    Checklist deleteChecklistByTaskIdAndChecklist(UUID taskId, UUID checklistId);
//
//    Checklist assignMemberToChecklist(UUID assignedByUserId, UUID assignToUserId, UUID checklistId, UUID taskId);
}