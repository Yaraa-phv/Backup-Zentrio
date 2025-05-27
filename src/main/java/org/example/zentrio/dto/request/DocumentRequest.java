package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.zentrio.enums.FileTypes;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentRequest {

    @NotBlank
    @NotNull
  private String accessToken;

    @NotBlank
    @NotNull
  private String folderName;

    @NotNull
  private FileTypes types;

    @NotNull
  private UUID boardId;
}
