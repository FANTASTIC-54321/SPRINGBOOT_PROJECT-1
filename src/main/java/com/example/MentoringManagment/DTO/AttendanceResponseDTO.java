package com.example.MentoringManagment.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AttendanceResponseDTO {

    private LocalDate sessionDate;
    private Boolean isPresent;

    //    public AttendanceDTO(LocalDate sessionDate, Boolean isPresent) {
//        this.sessionDate = sessionDate;
//        this.isPresent = isPresent;
//    }
}
