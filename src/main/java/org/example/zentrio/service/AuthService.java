package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.*;
import org.example.zentrio.dto.response.AppUserResponse;
import org.example.zentrio.dto.response.TokenResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public interface AuthService {

    AppUserResponse register(AppUserRequest request);

    TokenResponse login(AuthRequest request);

    void verify(String email, String otp);

    void resend(String email);

    UUID getCurrentAppUserId();

    AppUserResponse registerThirdParty(ThirdPartyRequest request);

    TokenResponse loginThirdParty(AuthThirdPartyRequest request) throws GeneralSecurityException, IOException;

    ResetPasswordRequest resetPassword(ResetPasswordRequest request,String email);

    void otpResetPassword(String email);

    void verifyReset(String email, String otp);
}
