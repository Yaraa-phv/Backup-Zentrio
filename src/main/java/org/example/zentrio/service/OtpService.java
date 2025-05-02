package org.example.zentrio.service;

public interface OtpService {
    String generateOtp(String email);
    Boolean validateOtp(String email, String inputOtp);
}
