package com.example.MentoringManagment.Controller;

import com.example.MentoringManagment.DTO.MentorRequestDTO;
import com.example.MentoringManagment.DTO.MentorRequestResponseDTO;
import com.example.MentoringManagment.Entity.MentorRequest;
import com.example.MentoringManagment.Service.MentorRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor-requests")
public class MentorRequestController {

    @Autowired
    private MentorRequestService mentorRequestService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestBody MentorRequestDTO requestDTO){
        MentorRequest saved = mentorRequestService.createRequest(requestDTO);
        return ResponseEntity.ok("Mentor request submitted with ID: " + saved.getRequestId());
    }

    @GetMapping("/pending")
    public List<MentorRequestResponseDTO> getPendingRequests() {
        return mentorRequestService.getPendingRequests();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        mentorRequestService.approveRequest(id);
        return ResponseEntity.ok("Mentor request approved");
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        mentorRequestService.rejectRequest(id);
        return ResponseEntity.ok("Mentor request rejected");
    }

}
