package com.example.MentoringManagment.DTO;

import com.example.MentoringManagment.Request.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Admin view
public class MentorRequestResponseDTO {
    private Long requestId;
    private Long userId;
    private String username;
    private String email;
    private String department;
    private String bio;
    private String skills;
    private RequestStatus status;
    private LocalDateTime requestedAt;
}
