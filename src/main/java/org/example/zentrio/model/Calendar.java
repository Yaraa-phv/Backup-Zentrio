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
public class Calendar {
    private UUID calendarId;
    private String noted;
    private LocalDateTime notedAt;
    private LocalDateTime tillDate;
    private UUID checkListId;
    private UUID userId;
    private UUID commentId;

}
