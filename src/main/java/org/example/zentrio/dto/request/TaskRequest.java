package org.example.zentrio.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank(message = "Task title cannot be blank!")
    private String title;
    private String description;
    @FutureOrPresent(message = "Start Date better in present or future!")
    @JsonSerialize
    @JsonFormat
    private LocalDateTime startedAt;
    @FutureOrPresent(message = "Start Date better in present or future!")
    @JsonFormat
    private LocalDateTime finishedAt;
}