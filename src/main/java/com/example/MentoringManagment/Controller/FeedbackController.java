package com.example.MentoringManagment.Controller;


import com.example.MentoringManagment.DTO.FeedbackDTO;
import com.example.MentoringManagment.DTO.FeedbackResponseDTO;
import com.example.MentoringManagment.DTO.FeedbackUpdateDTO;
import com.example.MentoringManagment.Service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<FeedbackResponseDTO> createFeedback(@Valid @RequestBody FeedbackDTO request){
        return ResponseEntity.ok(feedbackService.createFeedback(request));
    }

    @PatchMapping("/update/{feedbackId}")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(@PathVariable Long feedbackId ,@Valid @RequestBody FeedbackUpdateDTO request){
        return ResponseEntity.ok(feedbackService.updateFeedback(feedbackId,request));
    }

    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Feedback deleted successfully");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByStudent(studentId));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByTask(taskId));
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByRating(@PathVariable Integer rating) {
        return ResponseEntity.ok(feedbackService.getFeedbackByRating(rating));
    }

    @GetMapping("/mentorship/{mentorshipId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByMentorship(@PathVariable Long mentorshipId){
        List<FeedbackResponseDTO> saved = feedbackService.getFeedbackByMentorship(mentorshipId);
        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }







}



