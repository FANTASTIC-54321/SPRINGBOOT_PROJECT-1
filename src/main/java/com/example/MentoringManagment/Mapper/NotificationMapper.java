package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.NotificationRequestDTO;
import com.example.MentoringManagment.DTO.NotificationResponseDTO;
import com.example.MentoringManagment.Entity.Notification;

import java.time.Duration;
import java.time.LocalDateTime;

public class NotificationMapper {
    public static Notification toEntity(NotificationRequestDTO dto) {

        Notification notification = new Notification();
        notification.setReceiverId(dto.getReceiverId());
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
//        notification.setScheduledAt(dto.getScheduledAt());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        return notification;
    }

    public static NotificationResponseDTO toDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
//        dto.setScheduledAt(notification.getScheduledAt());
        dto.setTimeAgo(formatTimeAgo(notification.getCreatedAt()));
        return dto;
    }

    private static String formatTimeAgo(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());

        long seconds = duration.getSeconds();
        if (seconds < 60) return "just now";
        if (seconds < 3600) return seconds / 60 + " minutes ago";
        if (seconds < 86400) return seconds / 3600 + " hours ago";
        if (seconds < 172800) return "yesterday";
        return createdAt.toLocalDate().toString(); // fallback to date
    }


}
