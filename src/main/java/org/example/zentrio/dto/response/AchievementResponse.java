package org.example.zentrio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementResponse {
    private AppUserResponse user;
    private List<BoardSummary> ownBoards;
    private List<BoardSummary> joinedBoards;
}
