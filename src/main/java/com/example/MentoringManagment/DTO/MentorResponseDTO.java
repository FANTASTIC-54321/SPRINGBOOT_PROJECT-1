package com.example.MentoringManagment.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorResponseDTO {

        private String username;
        private String email;
        private String role;
        private String department;
        private Long phone;


    }


