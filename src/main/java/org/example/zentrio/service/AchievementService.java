package org.example.zentrio.service;


import org.example.zentrio.dto.request.AchievementRequest;
import org.example.zentrio.model.Achievement;

import java.util.HashSet;

public interface AchievementService {
    Achievement createAchievement(AchievementRequest achievementRequest);

    Achievement updateAchievement(AchievementRequest achievementRequest);

    Achievement getAllAchievementByCurrentUser();

    Achievement getAllAchievements();
}
