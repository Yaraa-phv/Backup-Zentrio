package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {
    private UUID attachmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, String> details;
    private UUID checklistId;
}
