package com.example.MentoringManagment.Request;


import lombok.Data;
import org.springframework.lang.NonNull;
import jakarta.validation.constraints.NotNull;


@Data
public class AssignRequest {

    @NotNull(message = "Mentor ID is required")
    private Long mentorId;

    @NotNull(message = "Student ID is required")
    private Long studentId;
}
