package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.NoteResponseDTO;
import com.example.MentoringManagment.Entity.Note;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

public class NoteMapper {
    public static NoteResponseDTO toDTO (Note note){
        NoteResponseDTO convert = new NoteResponseDTO();
        convert.setId(note.getId());
        convert.setDescription(note.getDescription());
        convert.setTitle(note.getTitle());
        convert.setUploadDate(note.getUploadDate());
        convert.setMentorName(note.getMentor().getUsername());
        convert.setFileUrl(note.getFileUrl());

        return convert;
    }
}
