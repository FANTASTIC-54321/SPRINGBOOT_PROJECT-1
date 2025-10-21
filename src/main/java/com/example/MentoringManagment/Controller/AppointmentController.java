package com.example.MentoringManagment.Controller;


import com.example.MentoringManagment.DTO.AppointmentRequestDTO;
import com.example.MentoringManagment.DTO.AppointmentResponseDTO;
import com.example.MentoringManagment.Request.AppointmentStatus;
import com.example.MentoringManagment.Request.UpdateAppointmentStatusDTO;
import com.example.MentoringManagment.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/request")
    public ResponseEntity<AppointmentResponseDTO> requestAppointment(@RequestBody AppointmentRequestDTO dto) {
        AppointmentResponseDTO response = appointmentService.requestAppointment(dto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/mentor-weekly")
    public ResponseEntity<Map<LocalDate, List<AppointmentResponseDTO>>> getMentorWeeklyAppointments(
            @RequestParam Long mentorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestHeader("X-User-Id") Long userId
    ) {
        Map<LocalDate, List<AppointmentResponseDTO>> response = appointmentService.getMentorWeeklyAppointments(mentorId, startDate, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-status")
    public ResponseEntity<AppointmentResponseDTO> updateAppointmentStatus(@RequestBody UpdateAppointmentStatusDTO dto) {
        AppointmentResponseDTO response = appointmentService.updateAppointmentStatus(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mentor/missed")
    public ResponseEntity<List<AppointmentResponseDTO>> getMissedAppointments(@RequestParam Long mentorId) {
        return ResponseEntity.ok(appointmentService.getMissedAppointments(mentorId));
    }

    @GetMapping("/mentor/status")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByStatus(
            @RequestParam Long mentorId,
            @RequestParam AppointmentStatus status
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(mentorId, status));
    }

    @GetMapping("/mentor/today")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForToday(@RequestParam Long mentorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsForToday(mentorId));
    }

}

/// ResponseEntity: This is a container for the entire HTTP response
///, allowing you to set the HTTP status code (like 200 OK) and headers, in addition to the body


// Map<LocalDate, List<AppointmentResponseDTO>>: This is the data structure in the response body.
// It means the response will be a map where the key is a LocalDate (representing the day, e.g., 2024-10-15)
// and the value is a List of appointments (AppointmentResponseDTO) scheduled for that specific day.