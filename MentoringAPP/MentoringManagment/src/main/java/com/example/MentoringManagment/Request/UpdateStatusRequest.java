package com.example.MentoringManagment.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UpdateStatusRequest {

    @NotBlank(message = "Status must not be empty")
    private String status;
}
