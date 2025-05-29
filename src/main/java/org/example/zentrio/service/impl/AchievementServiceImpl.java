package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.AchievementRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.Achievement;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.repository.AchievementRepository;
import org.example.zentrio.service.AchievementService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;

    @Override
    public Achievement createAchievement(AchievementRequest achievementRequest) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        boolean isAlreadySetup = achievementRepository.isAchievementExist(userId);
        if (isAlreadySetup) {
            throw new BadRequestException("You're already set up this achievement can't set up it again thank.");
        }
        return achievementRepository.createAchievement(achievementRequest, userId);
    }

    @Override
    public Achievement updateAchievement(AchievementRequest achievementRequest) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return achievementRepository.updateAchievement(achievementRequest,userId,LocalDateTime.now());
    }

    @Override
    public Achievement getAllAchievementByCurrentUser() {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return achievementRepository.getAllAchievementByCurrentUser(userId);
    }

    @Override
    public Achievement getAllAchievements() {
        return achievementRepository.getAllAchievements();
    }

    @Override
    public void deleteAchievement(UUID achievementId) {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Achievement achievement = achievementRepository.getAchievementById(achievementId);
        if (achievement == null) {
            throw new NotFoundException("Achievement not found");
        }
        achievementRepository.deletedAchievement(achievementId,userId);
    }

}
