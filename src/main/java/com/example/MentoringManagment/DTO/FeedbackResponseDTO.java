package com.example.MentoringManagment.DTO;

import lombok.Data;

@Data
public class FeedbackResponseDTO {

    private Long id;
    private Long mentorId;
    private Long studentId;
    private Long taskId;
    private Long mentorshipId;
    private Integer rating;
    private String comments;

}
