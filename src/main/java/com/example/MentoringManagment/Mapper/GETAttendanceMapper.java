package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.AttendanceResponseDTO;
import com.example.MentoringManagment.Entity.Attendance;

public class GETAttendanceMapper {

    public static AttendanceResponseDTO toDTO(Attendance attendance) {
        return new AttendanceResponseDTO(attendance.getSessionDate(), attendance.isPresent());
    }
}
