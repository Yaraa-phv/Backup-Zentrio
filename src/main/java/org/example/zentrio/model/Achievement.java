package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Achievement {
    private UUID achievementId;
//    private String achievementName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, Object> details;
    private UUID userId;

}
