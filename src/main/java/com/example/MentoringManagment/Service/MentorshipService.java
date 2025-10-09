package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.MentorshipDTO;
import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.MentorshipMapper;
import com.example.MentoringManagment.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MentorshipService {
    @Autowired
    private MentorshipRepository mentorshipRepository;

    @Autowired
    private UserRepository userRepository;

    public Mentorship assignMentorship(Long mentorId, Long studentId){

        if (mentorId.equals(studentId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentor and student cannot be the same user.");

        }

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceAccessException("Mentor not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceAccessException("Student not found"));

        String mentorRole = mentor.getRole();
        String studentRole = student.getRole();

        // Enforce valid role combinations
        if (!mentorRole.equals("MENTOR") || !studentRole.equals("MENTEE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentorship must be from a MENTOR to a STUDENT only.");
        }

        // Prevent duplicate assignments
        if (mentorshipRepository.findByStudent_UserId(studentId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This student is already assigned.");
        }

        Mentorship mentorship = new Mentorship();
        mentorship.setMentor(mentor);
        mentorship.setStudent(student);
        mentorship.setMentorshipStartDate(LocalDate.now());
        mentorship.setActive(true);

        return mentorshipRepository.save(mentorship);
    }

    public List<Mentorship> getAllMentorship(){
        return mentorshipRepository.findAll();
    }

    public Optional<Mentorship> getMentorshipById(long id) {
        return mentorshipRepository.findById(id);
    }


    public boolean deleteMentorshipById(long id) {
        Optional<Mentorship> mentorship = mentorshipRepository.findById(id);

        if(mentorship.isEmpty()){
            return false;
        }
        mentorshipRepository.deleteById(id);
        return true;
    }


    public Mentorship updateMentorship(Long id, MentorshipDTO dto) {
        Mentorship mentorship = mentorshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship not found with ID :- "+id));

        mentorship.setActive(dto.getActive());
        mentorship.setMentorshipEndDate(dto.getMentorshipEndDate());

        // Use the mapper to copy fields from DTO to entity
        MentorshipMapper.updateEntityFromDTO(dto, mentorship);

        return mentorshipRepository.save(mentorship);
    }

}
