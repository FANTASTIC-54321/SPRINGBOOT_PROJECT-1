package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.FeedbackDTO;
import com.example.MentoringManagment.DTO.FeedbackResponseDTO;
import com.example.MentoringManagment.DTO.FeedbackUpdateDTO;
import com.example.MentoringManagment.Entity.FeedBack;
import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.Tasks;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.FeedbackMapper;
import com.example.MentoringManagment.Repository.FeedbackRepository;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import com.example.MentoringManagment.Repository.TaskRepository;
import com.example.MentoringManagment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MentorshipRepository mentorshipRepository;


    public FeedbackResponseDTO createFeedback(FeedbackDTO request){

        User mentor = userRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found"));

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        Tasks task = taskRepository.findById(request.getTaskId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not Found"));

        Mentorship mentorship = mentorshipRepository.findById(request.getMentorshipId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Mentorship relation not found"));

        if(!"MENTOR".equalsIgnoreCase(mentor.getRole())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only mentors can give feedback");
        }

        FeedBack feedBack = new FeedBack();
        feedBack.setMentor(mentor);
        feedBack.setStudent(student);
        feedBack.setTask(task);
        feedBack.setMentorship(mentorship);
        feedBack.setRating(request.getRating());
        feedBack.setComments(request.getComments());

        return FeedbackMapper.toDTO(feedbackRepository.save(feedBack));

    }

    public FeedbackResponseDTO updateFeedback(Long feedbackId, FeedbackUpdateDTO request){
        FeedBack feedBack = feedbackRepository.findById(feedbackId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,""));

        User mentor = userRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found"));

        ///  I am confused about this , need to research more
//        if (!request.getMentorId().equals(mentor.getUserId())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
//                    "You are not the assigned mentor for this mentorship");
//        }

        if(!"MENTOR".equalsIgnoreCase(mentor.getRole())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only mentors can give feedback");
        }

        if(request.getRating() != null){
            feedBack.setRating(request.getRating());
        }

        if(request.getComments() != null){
            feedBack.setComments(request.getComments());
        }

        return FeedbackMapper.toDTO(feedbackRepository.save(feedBack));


    }

    public void deleteFeedback(Long feedbackId){
        if (!feedbackRepository.existsById(feedbackId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found");
        }
        feedbackRepository.deleteById(feedbackId);
    }

    public List<FeedbackResponseDTO> getFeedbackByStudent(Long studentId) {
        return feedbackRepository.findByMentorship_Student_UserId(studentId)
                .stream()
                .map(FeedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDTO> getFeedbackByTask(Long taskId) {
        return feedbackRepository.findByTask_Id(taskId)
                .stream()
                .map(FeedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDTO> getFeedbackByRating(Integer rating) {
        return feedbackRepository.findByRating(rating)
                .stream()
                .map(FeedbackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDTO> getFeedbackByMentorship(Long mentorshipId){
        return feedbackRepository.findByMentorship_Id(mentorshipId)
                .stream()
                .map(FeedbackMapper::toDTO)
                .collect(Collectors.toList());
    }


}
