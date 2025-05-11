package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementRequest {

    @NotNull
    @NotBlank
    private String achievementName;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
//    private OffsetDateTime createdAt;
//
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
//    private OffsetDateTime updatedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull
    @Size(min = 1, max = 100)
    private Map<String, Object> details;
}
