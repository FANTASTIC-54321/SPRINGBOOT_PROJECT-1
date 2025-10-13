package com.example.MentoringManagment.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponseDTO {
    private Long id ;


    private String mentorName;


    private String title;


    private String description;


    private String fileUrl;


    private LocalDate uploadDate;
}
