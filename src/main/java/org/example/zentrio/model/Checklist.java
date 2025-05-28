package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.dto.response.MemberResponse;
import org.example.zentrio.dto.response.MemberResponseData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Checklist {
    private UUID checklistId;
    private String title;
    private String status;
    private String cover;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer checklistOrder;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private List<MemberResponseData> members;
    private UUID taskId;
    private UUID createdBy;
}