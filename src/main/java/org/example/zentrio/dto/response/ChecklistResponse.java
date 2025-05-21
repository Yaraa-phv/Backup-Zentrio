package org.example.zentrio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistResponse {
    private UUID checklistId;
    private String title;
    private List<String>  members;
    private Integer comments;
    private Map<String, Object> attachments;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private UUID taskId;
}