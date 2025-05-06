package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
    private UUID feedbakId;
    private LocalDateTime createAt;
    private String comment;
    private UUID taskId;
    private UUID userId;
}
