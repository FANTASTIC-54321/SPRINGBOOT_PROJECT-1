package com.example.MentoringManagment.DTO;

import com.example.MentoringManagment.Request.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {
    private Long id;
    private String mentorName;
    private String studentName;
    private LocalDateTime requestedTime;
    private AppointmentStatus status;
    private String rejectionReason;

}
