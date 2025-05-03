package org.example.zentrio.service;

import java.util.Map;

public interface EmailService {
    void sendEmail(String otpCode, String email);
    void sendDynamicEmail(String otpCode, String templateName,String subject, Map<String, Object> variables);
}
