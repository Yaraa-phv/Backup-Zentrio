package org.example.zentrio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.model.Notification;
import org.example.zentrio.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/sendMessageToAllUsers")
    public void sendMessageToAllUsers(@RequestBody String message) throws JsonProcessingException {
        notificationService.sendMessageToAllUsers(message);
    }

    @PostMapping("/sendMessageToUser/{userId}/tasks/{task-id}")
    public void sendMessageToUser(@PathVariable("userId") String userId,
                                  @PathVariable("task-id") UUID taskId,
                                  @RequestBody String message) throws JsonProcessingException {
        notificationService.sendMessageToUser(userId,taskId,message);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<HashSet<Notification>> getNotificationByUserId(@PathVariable("userId") UUID userId) {
        HashSet<Notification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

}
