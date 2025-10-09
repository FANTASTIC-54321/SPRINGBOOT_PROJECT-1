package com.example.MentoringManagment.DTO;

import com.example.MentoringManagment.Entity.Mentorship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NoticeResponseDTO {

    private Long id ;


    private String mentorName;


    private String title;


    private String description;


    private String fileUrl;


    private LocalDateTime uploadDate;
}
