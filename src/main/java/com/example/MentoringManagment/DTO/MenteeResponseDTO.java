package com.example.MentoringManagment.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenteeResponseDTO {
    private String username;
    private String email;
    private String role;
    private String department;
    private Long phone;
    private int semester;

}
