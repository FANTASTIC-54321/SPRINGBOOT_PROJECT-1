package com.example.MentoringManagment.DTO;

import com.example.MentoringManagment.Request.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
        @NotNull
        private Long receiverId;
        @NotNull
        private String message;
        @NotNull
        private NotificationType type;

//        private LocalDateTime scheduledAt; // optional

}
