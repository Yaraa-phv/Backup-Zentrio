package org.example.zentrio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteBoard {
    private UUID favoriteId;
    private UUID userId;
    private UUID boardId;
    private String markedAt;
}
