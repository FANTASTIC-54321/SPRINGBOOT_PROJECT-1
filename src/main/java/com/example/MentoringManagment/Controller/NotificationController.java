package com.example.MentoringManagment.Controller;

import com.example.MentoringManagment.DTO.NotificationRequestDTO;
import com.example.MentoringManagment.DTO.NotificationResponseDTO;
import com.example.MentoringManagment.Entity.Notification;
import com.example.MentoringManagment.Mapper.NotificationMapper;
import com.example.MentoringManagment.Service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> createNotification(@Valid @RequestBody NotificationRequestDTO dto) {
        notificationService.createNotification(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Notification created successfully");
    }

    @GetMapping("/{receiverId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications(@PathVariable Long receiverId) {
        List<Notification> notifications = notificationService.getUserNotifications(receiverId);
        List<NotificationResponseDTO> responseDTOs = notifications.stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());

        if (responseDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{receiverId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long receiverId) {
        Long count = notificationService.getUnreadCount(receiverId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/mark-read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }

}
