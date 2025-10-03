package com.example.MentoringManagment.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendancePatchDTO {
    private LocalDate sessionDate;
    private Boolean isPresent;

    @NotNull(message = "MENTOR ID is required")
    private Long userId;

}
