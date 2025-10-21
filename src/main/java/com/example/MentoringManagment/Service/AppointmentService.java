package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.AppointmentRequestDTO;
import com.example.MentoringManagment.DTO.AppointmentResponseDTO;
import com.example.MentoringManagment.DTO.NotificationRequestDTO;
import com.example.MentoringManagment.Entity.Appointment;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.AppointmentMapper;
import com.example.MentoringManagment.Repository.AppointmentRepository;
import com.example.MentoringManagment.Repository.UserRepository;
import com.example.MentoringManagment.Request.AppointmentStatus;
import com.example.MentoringManagment.Request.NotificationType;
import com.example.MentoringManagment.Request.UpdateAppointmentStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.MentoringManagment.Mapper.AppointmentMapper.toDTO;

@Service
public class AppointmentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    public AppointmentResponseDTO requestAppointment(AppointmentRequestDTO dto) {
        User mentor = userRepository.findById(dto.getMentorId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Mentor not found"));

        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found"));

//        if (!isWithinAvailability(mentor.getDepartment(), dto.getRequestedTime())) {
//            throw new IllegalArgumentException("Requested time is outside mentor's availability");
//        }

        boolean alreadyBooked = appointmentRepository.existsByMentorAndRequestedTimeAndStatus(
                mentor, dto.getRequestedTime(), AppointmentStatus.APPROVED);

        if (alreadyBooked) {
            throw new IllegalArgumentException("Slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setMentor(mentor);
        appointment.setStudent(student);
        appointment.setRequestedTime(dto.getRequestedTime());
        appointment.setStatus(AppointmentStatus.PENDING);

        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setReceiverId(student.getUserId());
        notification.setType(NotificationType.APPOINTMENT);
        notification.setMessage("Your appointment was " + AppointmentStatus.PENDING.name().toLowerCase());
        notificationService.createNotification(notification);


        Appointment saved =  appointmentRepository.save(appointment);
        return toDTO(saved);
    }

    public Map<LocalDate, List<AppointmentResponseDTO>> getMentorWeeklyAppointments(Long mentorId, LocalDate startDate, Long
            userId) {
        if (!mentorId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Mentors can only view their own appointments");
        }

        User mentor = userRepository.findById(mentorId).orElseThrow();
        Map<LocalDate, List<AppointmentResponseDTO>> weeklyAppointments = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            List<Appointment> appointments = appointmentRepository.findByMentorAndRequestedTimeBetween(
                    mentor, startOfDay, endOfDay);

            List<AppointmentResponseDTO> dtos = appointments.stream()
                    .map(AppointmentMapper::toDTO)
                    .collect(Collectors.toList());

            weeklyAppointments.put(date, dtos);
        }

        return weeklyAppointments;
    }

    public AppointmentResponseDTO updateAppointmentStatus(UpdateAppointmentStatusDTO updateAppointmentStatusDTO){
        // Auto-reject any expired pending appointments before proceeding
        autoRejectExpiredAppointments();

        Appointment appointment = appointmentRepository.findById(updateAppointmentStatusDTO.getAppointmentId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Appointment not found"));

        if (!appointment.getMentor().getUserId().equals(updateAppointmentStatusDTO.getMentorId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Mentors can only update their own appointments");
        }

        if(appointment.getStatus()!=AppointmentStatus.PENDING){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending appointments can be updated");
        }

        if (appointment.getRequestedTime().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment slot has expired and cannot be updated");
        }

        if (updateAppointmentStatusDTO.getStatus() == AppointmentStatus.APPROVED){
            appointment.setStatus(AppointmentStatus.APPROVED);
            appointment.setRejectionReason(null);
        } else if (updateAppointmentStatusDTO.getStatus() == AppointmentStatus.REJECTED) {
            appointment.setStatus(AppointmentStatus.REJECTED);
            appointment.setRejectionReason(updateAppointmentStatusDTO.getRejectionReason());
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status update");
        }

        Appointment updated = appointmentRepository.save(appointment);
        return toDTO(updated,updateAppointmentStatusDTO.getMentorId());
    }

    public List<AppointmentResponseDTO> getMissedAppointments(Long mentorId) {
//        if (!mentorId.equals(userId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Mentors can only view their own appointments");
//        }
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor Id with " + mentorId + " not found"));

        LocalDateTime now = LocalDateTime.now();

        List<Appointment> missed = appointmentRepository
                .findByMentorAndRequestedTimeBeforeAndStatus(mentor, now, AppointmentStatus.PENDING);

        return missed.stream().map(app -> {
            app.setStatus(AppointmentStatus.REJECTED);
            app.setRejectionReason("Auto-rejected: missed slot");
            return appointmentRepository.save(app);
        }).map(app -> AppointmentMapper.toDTO(app, mentorId)).collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getAppointmentsByStatus(Long mentorId, AppointmentStatus status) {
//        if (!mentorId.equals(userId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Mentors can only view their own appointments");
//        }
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor Id with " + mentorId + " not found"));
        return appointmentRepository.findByMentorAndStatus(mentor, status)
                .stream().map(app -> AppointmentMapper.toDTO(app, mentorId))
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getAppointmentsForToday(Long mentorId) {
        User mentor = userRepository.findById(mentorId).orElseThrow();
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        return appointmentRepository.findByMentorAndRequestedTimeBetween(mentor, start, end)
                .stream().map(appointment -> AppointmentMapper.toDTO(appointment, mentorId))
                .collect(Collectors.toList());
    }

    /// - Find expired appointments:
    /// - It queries all appointments where:
    /// - status == PENDING
    /// - requestedTime < now (i.e., the scheduled time has already passed)
    ///
    /// - Loop through each expired appointment:
    /// - Sets its status to REJECTED
    /// - Adds a rejection reason: "Auto-rejected: slot expired"
    @Scheduled(fixedRate = 1800000)     // - Spring will automatically run this method every 30 minutes after startup
    public void autoRejectExpiredAppointments() {
        List<Appointment> expired = appointmentRepository.findByStatusAndRequestedTimeBefore(
                AppointmentStatus.PENDING, LocalDateTime.now());

        for (Appointment appointment : expired) {
            appointment.setStatus(AppointmentStatus.REJECTED);
            appointment.setRejectionReason("Auto-rejected: slot expired");
        }

        appointmentRepository.saveAll(expired);
    }

}

/// Without verifying that the caller is the same mentor whose data
///  is being requested, any user could query any mentor’s schedule just by passing their mentorId.

//  // ✅ Direct expiration check using if-else
//    if (appointment.getRequestedTime().isBefore(LocalDateTime.now())) {
//        appointment.setStatus(AppointmentStatus.REJECTED);
//        appointment.setRejectionReason("Auto-rejected: slot expired");
//        Appointment updated = appointmentRepository.save(appointment);
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment slot has expired and was auto-rejected");
//    }
