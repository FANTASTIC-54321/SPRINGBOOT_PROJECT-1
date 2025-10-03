package com.example.MentoringManagment.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRequest {

    @NotNull(message =  "mentorId is required")
    private Long mentorId;

    @NotNull(message = "studentId is required")
    private Long studentId;
}



