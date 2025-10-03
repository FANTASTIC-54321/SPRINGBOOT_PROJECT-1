package com.example.MentoringManagment.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;


    private String email;


    private String role;


    private String Department;


    private Long Phone;


    private int semester;
}
