package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AchievementRequest {

    @NotNull
    @Size(min = 1, max = 100)
    private Map<String, Object> details;
}
