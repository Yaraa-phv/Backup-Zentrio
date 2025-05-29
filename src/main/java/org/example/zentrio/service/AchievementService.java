package org.example.zentrio.service;


import org.example.zentrio.dto.request.AchievementRequest;
import org.example.zentrio.model.Achievement;

import java.util.HashSet;
import java.util.UUID;

public interface AchievementService {
    Achievement createAchievement(AchievementRequest achievementRequest);

    Achievement updateAchievement(AchievementRequest achievementRequest);

    Achievement getAllAchievementByCurrentUser();

    Achievement getAllAchievements();

    void deleteAchievement(UUID achievementId);
}
