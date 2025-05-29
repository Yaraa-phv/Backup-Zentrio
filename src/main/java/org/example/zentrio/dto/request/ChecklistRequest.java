package org.example.zentrio.dto.request;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ChecklistRequest {

    @NotNull
    @NotBlank
    private String title;

    private String description;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startedAt;

    @NotNull
    @FutureOrPresent
    private LocalDateTime finishedAt;

    @NotNull
    private UUID taskId;

}