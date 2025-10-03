package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.FeedbackResponseDTO;
import com.example.MentoringManagment.Entity.FeedBack;

public class FeedbackMapper {

    public static FeedbackResponseDTO toDTO (FeedBack feedBack){
        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();

        feedbackResponseDTO.setId(feedBack.getId());
        feedbackResponseDTO.setMentorId(feedBack.getMentor().getUserId());
        feedbackResponseDTO.setStudentId(feedBack.getStudent().getUserId());
        feedbackResponseDTO.setTaskId(feedBack.getTask().getId());
        feedbackResponseDTO.setMentorshipId(feedBack.getMentorship().getId());
        feedbackResponseDTO.setComments(feedBack.getComments());
        feedbackResponseDTO.setRating(feedBack.getRating());

        return feedbackResponseDTO;

    }
}




