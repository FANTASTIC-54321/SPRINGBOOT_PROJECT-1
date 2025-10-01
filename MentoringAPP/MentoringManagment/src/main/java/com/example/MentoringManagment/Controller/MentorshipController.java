package com.example.MentoringManagment.Controller;


import com.example.MentoringManagment.DTO.MentorshipDTO;
import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Request.AssignRequest;
import com.example.MentoringManagment.Service.MentorshipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mentorship")
public class MentorshipController {


    @Autowired
    private MentorshipService mentorshipService;

    @PostMapping("/assign")
    public ResponseEntity<Mentorship> assign(@Valid @RequestBody AssignRequest req) {

//        if (req.getMentorId() == null || req.getStudentId() == null) {
//            return ResponseEntity.badRequest().body("Mentor ID and Student ID must not be null");
//        }
        Mentorship mentorship = mentorshipService.assignMentorship(req.getMentorId(), req.getStudentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorship);
    }

    @GetMapping("/mentorships")
    public List<Mentorship> getAllMentorship(){

        return mentorshipService.getAllMentorship();
    }

    @GetMapping("/mentorships/{id}")
    public ResponseEntity<?> getMentorshipById(@PathVariable long id){
        // Use ResponseEntity<?> if you want to return different types (like error messages)
        Optional<Mentorship> mentorship = mentorshipService.getMentorshipById(id);

        if(mentorship.isEmpty()){
            String errorMessage = "Mentorship with ID " + id + " not found.";
            System.out.println(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
        return ResponseEntity.ok(mentorship.get());         // .get() is a predefined method in the Optional class in Java
        // It returns the actual value inside the Optional — in this case, a Mentorship object
    }

    @DeleteMapping("/mentorships/{id}")
    public ResponseEntity<?> deleteMentorshipById(@PathVariable long id){
        boolean deleted = mentorshipService.deleteMentorshipById(id);

        if(!deleted){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mentorship not found with ID " + id);
        }
        return ResponseEntity.ok("Mentorship deleted successfully.");
    }

    @PutMapping("/mentorships/{id}")
    public ResponseEntity<Mentorship> updateMentorshipById(
            @PathVariable Long id,
            @RequestBody MentorshipDTO mentorshipDTO) {

        Mentorship updatedMentorship = mentorshipService.updateMentorship(id, mentorshipDTO);
        return ResponseEntity.ok(updatedMentorship);
    }

}
