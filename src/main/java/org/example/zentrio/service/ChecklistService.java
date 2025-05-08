package org.example.zentrio.service;

import org.example.zentrio.dto.request.ChecklistRequest;
import org.example.zentrio.model.Checklist;
import org.example.zentrio.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface ChecklistService {
    Checklist createChecklist(UUID taskId, ChecklistRequest checklistRequest);

    UUID checkExistedTaskId(UUID taskId, UUID currentUserId);

    UUID checkTaskIdToGetChecklist(UUID taskId);

    HashMap<String, Checklist> getAllChecklistByTaskId(UUID taskId);

    Checklist getChecklistByTaskIdAndChecklistId(UUID taskId, UUID checklistId);

    HashMap<String, Checklist> getChecklistByTaskIdAndTitle(UUID taskId, String title);

    Checklist updateChecklistById(UUID taskId, UUID checklistId, ChecklistRequest checklistRequest);

    Checklist deleteChecklistByTaskIdAndChecklist(UUID taskId, UUID checklistId);

    Checklist assignMemberToChecklist(UUID assignedByUserId, UUID assignToUserId, UUID checklistId, UUID taskId);
}
