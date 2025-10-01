package com.example.MentoringManagment.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {

    @NotNull(message = "Mentorship ID is required to assign a task")
    private Long mentorshipId;

    @NotNull(message = "Assigner ID is required")
    private Long assignerId;

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private LocalDate dueDate;

    // Used for initial creation status or updating the status
    private String status;
}
