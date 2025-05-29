package org.example.zentrio.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CalendarTaskResponse {
    private UUID calendarId;
    private UUID taskId;
    private String title;
    private LocalDateTime notedAt;
    private LocalDateTime tillDate;
}
