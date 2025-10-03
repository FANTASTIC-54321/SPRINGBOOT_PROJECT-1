package com.example.MentoringManagment.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackUpdateDTO {
    @NotNull(message = "Mentor ID is required")
    private Long mentorId;

    private String comments;

    @Min(1)
    @Max(5)
    private Integer rating;
}
