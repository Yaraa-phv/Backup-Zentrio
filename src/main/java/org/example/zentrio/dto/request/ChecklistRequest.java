package org.example.zentrio.dto.request;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.model.Attachment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecklistRequest {

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