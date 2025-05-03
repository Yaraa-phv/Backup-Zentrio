package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.ProfileRequest;
import org.example.zentrio.dto.response.AppUserResponse;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.repository.AppUserRepository;
import org.example.zentrio.repository.ProfileRepository;
import org.example.zentrio.service.AuthService;
import org.example.zentrio.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;


    @Override
    public AppUserResponse getProfile() {

        AppUser appUser = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        AppUser appUser = appUserRepository.getUserByEmail(email);
        System.out.println("appUser" + appUser.getUsername());

        System.out.println("Email: " + appUser);

        return modelMapper.map(appUser, AppUserResponse.class);
    }

    @Override
    public AppUserResponse updateProfile(ProfileRequest profileRequest) {
        UUID appUserId = authService.getCurrentAppUserId();
        AppUser appUser = profileRepository.updateProfile(profileRequest,appUserId);
        return appUser != null ? modelMapper.map(appUser, AppUserResponse.class) : null;
    }

    @Override
    public void deleteProfile() {
        UUID appUserId = authService.getCurrentAppUserId();
        profileRepository.deleteProfile(appUserId);
    }


}
