package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GanttBarRequest {
    @NotNull(message = "title cannot null")
    @NotBlank
    private String title;

    private LocalDateTime startAt;
    private LocalDateTime finshedAt;


}
