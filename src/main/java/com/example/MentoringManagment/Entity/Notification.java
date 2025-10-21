package com.example.MentoringManagment.Entity;

import com.example.MentoringManagment.Request.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId; // mentee or mentor

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // TASK, APPOINTMENT, NOTICE, FEEDBACK, etc.

    private boolean isRead = false;

    private LocalDateTime createdAt;

//    private LocalDateTime scheduledAt;

}
