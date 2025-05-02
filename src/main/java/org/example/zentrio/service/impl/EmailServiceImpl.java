package org.example.zentrio.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @SneakyThrows
    @Override
    public void sendEmail(String otpCode, String email) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BadRequestException("Invalid email address");
        }

        try {
            Context context = new Context();
            context.setVariable("otp", otpCode);
            System.out.println("otpCode: " + otpCode);
            String process = templateEngine.process("index", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject("Verify your email with otp");
            mimeMessageHelper.setText(process, true);
            mimeMessageHelper.setTo(email);
            javaMailSender.send(mimeMessage);
        } catch (BadRequestException e) {
            throw new BadRequestException("Failed to send email");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email due to messaging error.", e);
        }

    }
}
