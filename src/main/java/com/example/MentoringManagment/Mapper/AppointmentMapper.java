package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.AppointmentRequestDTO;
import com.example.MentoringManagment.DTO.AppointmentResponseDTO;
import com.example.MentoringManagment.Entity.Appointment;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Request.AppointmentStatus;

public class AppointmentMapper {

    public static AppointmentResponseDTO toDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getMentor().getUsername(),
                appointment.getStudent().getUsername(),
                appointment.getRequestedTime(),
                appointment.getStatus(),
                appointment.getRejectionReason()

        );
    }

    public static AppointmentResponseDTO toDTO(Appointment appointment, Long viewerId) {
        boolean isMentee = appointment.getStudent().getUserId().equals(viewerId);
        boolean isMentor = appointment.getMentor().getUserId().equals(viewerId);

        String rejectionReason = (isMentee || isMentor) ? appointment.getRejectionReason() : null;

        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getMentor().getUsername(),
                appointment.getStudent().getUsername(),
                appointment.getRequestedTime(),
                appointment.getStatus(),
                rejectionReason
        );
    }

    public static Appointment toEntity(AppointmentRequestDTO dto, User mentor, User student) {
        Appointment appointment = new Appointment();
        appointment.setMentor(mentor);
        appointment.setStudent(student);
        appointment.setRequestedTime(dto.getRequestedTime());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setRejectionReason(null);
        return appointment;
    }

}
