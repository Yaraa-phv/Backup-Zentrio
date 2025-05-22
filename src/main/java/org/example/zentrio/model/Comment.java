package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.dto.response.MemberResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    private UUID id;
    private String content;
    private LocalDateTime creationDate;
    private UUID checklistId;
    private UUID commentBy;
    private MemberResponse  member;
}
