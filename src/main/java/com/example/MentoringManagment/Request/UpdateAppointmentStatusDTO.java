package com.example.MentoringManagment.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAppointmentStatusDTO {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Status is required")
    private AppointmentStatus status;

    // Optional rejection reason
    private String rejectionReason;

    @NotNull(message = "Mentor ID is required")
    private Long mentorId;
}
