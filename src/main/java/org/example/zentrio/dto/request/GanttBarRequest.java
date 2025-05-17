package org.example.zentrio.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.FutureOrPresent;
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

    @FutureOrPresent
    private LocalDateTime startedAt;

    @FutureOrPresent
    private LocalDateTime finishedAt;


}
