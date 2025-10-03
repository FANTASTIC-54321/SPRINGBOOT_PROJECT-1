package com.example.MentoringManagment.Controller;


import com.example.MentoringManagment.DTO.AttendanceDTO;
import com.example.MentoringManagment.DTO.AttendancePatchDTO;
import com.example.MentoringManagment.DTO.AttendanceResponseDTO;
import com.example.MentoringManagment.Entity.Attendance;
import com.example.MentoringManagment.Service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/create")
    public ResponseEntity<Attendance> markAttendance(@Valid @RequestBody AttendanceDTO request){
        Attendance attendance = attendanceService.markAttendance(request);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/mentorship/{mentorshipId}")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByMentorshipId(@PathVariable Long mentorshipId ){
        List<AttendanceResponseDTO> attendanceList = attendanceService.getAttendanceByMentorshipId(mentorshipId);
        return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
    }

    @PatchMapping("/{attendanceId}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long attendanceId ,@Valid @RequestBody AttendancePatchDTO request){
        Attendance updatedAttendance = attendanceService.updateAttendance(attendanceId,request);
        return ResponseEntity.ok(updatedAttendance);
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long attendanceId){
        attendanceService.deleteAttendance(attendanceId);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Attendance Deleted Successfully ");
    }



}
