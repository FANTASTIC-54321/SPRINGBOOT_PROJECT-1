package com.example.MentoringManagment.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenteeRegisterDTO {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Department is mandatory")
    private String department;

    private String role;

    @NotNull(message = "Phone number is mandatory")
    private Long phone;

    @Min(value = 1, message = "Semester must be greater than 0")
    @Max(value = 8, message = "Semester must be Less than 8")
    private int semester;
}
