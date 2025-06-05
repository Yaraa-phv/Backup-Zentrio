package org.example.zentrio.service;


import org.example.zentrio.dto.request.ProfileRequest;
import org.example.zentrio.dto.response.AppUserResponse;

import java.util.UUID;

public interface ProfileService {
    AppUserResponse getProfile();

    AppUserResponse updateProfile(ProfileRequest profileRequest);

    void deleteProfile();

    AppUserResponse getProfileByUserId(UUID userId);
}
