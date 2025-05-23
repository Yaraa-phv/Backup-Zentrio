package org.example.zentrio.dto.request;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GanttBarRequest {
    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startedAt;

    @NotNull
    @FutureOrPresent
    private LocalDateTime finishedAt;


}
