package org.example.zentrio.dto.request;

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
public class WorkspaceRequest {
    @NotBlank(message = "Title cannot be blank!")
    private String title;
    private String description;
//    private LocalDateTime updatedAt;
}