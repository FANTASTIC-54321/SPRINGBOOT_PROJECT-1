package com.example.MentoringManagment.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceDTO {

    @NotNull(message = "Mentorship ID is required ")
    private Long mentorshipId;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Presence status (true/false) is required")
    private Boolean isPresent;

    @NotNull(message = "MENTOR ID is required")
    private Long userId;
}
