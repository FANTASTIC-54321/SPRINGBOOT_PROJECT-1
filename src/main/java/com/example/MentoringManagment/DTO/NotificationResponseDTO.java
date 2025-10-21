package com.example.MentoringManagment.DTO;

import com.example.MentoringManagment.Request.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {

        private Long id;
        private String message;
        private NotificationType type;
        private boolean read;
        private LocalDateTime createdAt;
//        private LocalDateTime scheduledAt;
        private String timeAgo; // 👈 new field

}
