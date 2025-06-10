package org.example.zentrio.service;

import org.example.zentrio.enums.RoleRequest;

import java.util.Map;
import java.util.UUID;

public interface EmailService {
    void sendEmail(String otpCode, String email);
    void sendDynamicEmail(String otpCode, String templateName,String subject, Map<String, Object> variables);
    void sendInvitations(UUID workspaceId,UUID boardId, String email, RoleRequest role);
}
