package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentRequest {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull
    @Size(min = 1, max = 100)
    private Map<String, String> details;
}
