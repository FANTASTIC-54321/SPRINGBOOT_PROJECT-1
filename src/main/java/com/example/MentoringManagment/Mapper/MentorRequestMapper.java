package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.MentorRequestDTO;
import com.example.MentoringManagment.DTO.MentorRequestResponseDTO;
import com.example.MentoringManagment.Entity.MentorRequest;
import com.example.MentoringManagment.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class MentorRequestMapper {

    public static MentorRequestResponseDTO toResponseDTO(MentorRequest request) {
        User user = request.getUser();
        return new MentorRequestResponseDTO(
                request.getRequestId(),
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getDepartment(),
                request.getBio(),
                request.getSkills(),
                request.getStatus(),
                request.getRequestedAt()
        );
    }
}
