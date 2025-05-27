package org.example.zentrio.dto.request;

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
public class WorkspaceRequest {
    @NotBlank(message = "Title cannot be blank!")
    @NotNull
    private String title;

    @NotBlank(message = "description cannot be blank!")
    @NotNull
    private String description;
}