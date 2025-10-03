package com.example.MentoringManagment.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackDTO {

    @NotNull(message = "Mentor ID is required")
    private Long mentorId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotNull(message = "Mentorship ID is required")
    private Long mentorshipId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Minimum rating is 1")
    @Max(value = 5, message = "Maximum rating is 5")
    private Integer rating;

    private String comments;

}
