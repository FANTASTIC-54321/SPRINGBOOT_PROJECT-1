package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.MentorshipDTO;
import com.example.MentoringManagment.Entity.Mentorship;

public class MentorshipMapper {

    public static void updateEntityFromDTO(MentorshipDTO dto, Mentorship mentorship) {
        mentorship.setMentorshipEndDate(dto.getMentorshipEndDate());
        mentorship.setActive(dto.getActive());
    }
}
