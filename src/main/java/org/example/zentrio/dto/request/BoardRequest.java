package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @NotBlank
    private String title;

    private String description;

    private String cover;


    private UUID workspaceId;
}