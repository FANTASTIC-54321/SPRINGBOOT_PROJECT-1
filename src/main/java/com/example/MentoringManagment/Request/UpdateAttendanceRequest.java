package com.example.MentoringManagment.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateAttendanceRequest {

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Presence status (true/false) is required")
    private Boolean isPresent;
}
