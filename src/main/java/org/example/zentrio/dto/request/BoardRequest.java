package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequest {
    @NotBlank(message = "Title cannot be blank!")
    private String title;
    private String description;
    private String cover;
    private Boolean isVerified;
    private UUID workspaceId;
}