package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.NoticeResponseDTO;
import com.example.MentoringManagment.Entity.Notice;

public class NoticeMapper {

    public static NoticeResponseDTO toDTO(Notice notice) {
        return new NoticeResponseDTO(
                notice.getId(),
                notice.getMentor().getUsername(), // For getting the name from user table
                notice.getTitle(),
                notice.getDescription(),
                notice.getFileUrl(),
                notice.getUploadDate()
        );
    }
}
