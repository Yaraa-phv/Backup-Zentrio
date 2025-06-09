package org.example.zentrio.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteBoardRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID boardId;

}
