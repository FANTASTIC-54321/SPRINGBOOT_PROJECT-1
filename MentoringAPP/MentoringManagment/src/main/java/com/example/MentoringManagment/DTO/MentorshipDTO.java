package com.example.MentoringManagment.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
//@RequiredArgsConstructor
@NoArgsConstructor
public class MentorshipDTO {

    private LocalDate mentorshipEndDate;
    private Boolean active;
}
