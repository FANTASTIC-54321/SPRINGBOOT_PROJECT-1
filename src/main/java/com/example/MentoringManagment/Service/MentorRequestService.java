package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.MentorRequestDTO;
import com.example.MentoringManagment.DTO.MentorRequestResponseDTO;
import com.example.MentoringManagment.Entity.MentorRequest;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.MentorRequestMapper;
import com.example.MentoringManagment.Repository.MentorRequestRepository;
import com.example.MentoringManagment.Repository.UserRepository;
import com.example.MentoringManagment.Request.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MentorRequestService {

    @Autowired
    private MentorRequestRepository mentorRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public MentorRequest createRequest(MentorRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (mentorRequestRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Mentor request already exists for this user");
        }

        MentorRequest request = new MentorRequest();
        request.setUser(user);
        request.setBio(dto.getBio());
        request.setSkills(dto.getSkills());
        request.setStatus(RequestStatus.PENDING);

        return mentorRequestRepository.save(request);
    }

    public List<MentorRequestResponseDTO> getPendingRequests() {
        return mentorRequestRepository.findByStatus(RequestStatus.PENDING)
                .stream()
                .map(MentorRequestMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void approveRequest(Long requestId) {
        MentorRequest request = mentorRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User user = request.getUser();
        user.setRole("MENTOR");
        user.setIsMentorApproved(true);
        request.setStatus(RequestStatus.APPROVED);

        userRepository.save(user);
        mentorRequestRepository.save(request);
    }

    public void rejectRequest(Long requestId) {
        MentorRequest request = mentorRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        mentorRequestRepository.save(request);
    }

}
