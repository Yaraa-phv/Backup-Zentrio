package org.example.zentrio.service;


import org.example.zentrio.dto.request.ProfileRequest;
import org.example.zentrio.dto.response.AppUserResponse;
import org.example.zentrio.model.AppUser;

public interface ProfileService {
    AppUserResponse getProfile();

    AppUserResponse updateProfile(ProfileRequest profileRequest);

    void deleteProfile();
}
