package com.example.MentoringManagment.Controller;


import com.example.MentoringManagment.DTO.NoteRequestDTO;
import com.example.MentoringManagment.DTO.NoteResponseDTO;
import com.example.MentoringManagment.Entity.Note;
import com.example.MentoringManagment.Mapper.NoteMapper;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import com.example.MentoringManagment.Repository.NoteRepository;
import com.example.MentoringManagment.Service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.MentoringManagment.Mapper.NoteMapper.toDTO;

@RestController
@RequestMapping("/api/note")
@RequiredArgsConstructor
public class NoteController {

    @Autowired
   private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MentorshipRepository mentorshipRepository;

    @PostMapping("/upload")
    public ResponseEntity<NoteResponseDTO> uploadNote(
            @RequestParam Long mentorId ,
            @RequestBody NoteRequestDTO requestDTO
            ) throws IOException {
        Note note = noteService.createNote(mentorId,requestDTO);
        NoteResponseDTO saved = toDTO(note);
        return ResponseEntity.ok(saved);
    }

    // Get all notes accessible to a mentee
    @GetMapping("/mentee/{menteeId}")
    public ResponseEntity<List<NoteResponseDTO>> getNotesForMentee(@PathVariable Long menteeId){
        List<Note> notes = noteService.getNoticesForMentee(menteeId);
        List<NoteResponseDTO> dtoList = notes.stream()
                .map(NoteMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Download the note PDF file by noteId
    @GetMapping("/download/{noteId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long noteId,@RequestParam Long userId) throws IOException{

        Note note = noteService.getNoteById(noteId);
        Long mentorId = note.getMentor().getUserId();
        Boolean authorized = false;

        if(userId.equals(mentorId)){
            authorized = true;
        }
        else {
            // or Check if the user is one of the mentor's assigned mentee
            authorized = mentorshipRepository
                    .findByMentor_UserIdAndStudent_UserId(mentorId,userId)
                    .isPresent();
        }

        if(!authorized){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN,"User not authorized to access the note");
        }

        Path filePath = Paths.get(note.getFileUrl());
        Resource resource = noteService.loadFileAsResource(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);

    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponseDTO>> searchNotes(
            @RequestParam Long mentorId,
            @RequestParam String keyword) {

        List<Note> notes = noteService.searchNotesByKeyword(mentorId, keyword);
        List<NoteResponseDTO> dtoList = notes.stream()
                .map(NoteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/filter-by-date")
    public ResponseEntity<List<NoteResponseDTO>> filterNotesByDate(
            @RequestParam Long mentorId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Use ISO format like '2025-10-01'");
        }

        List<Note> notes = noteService.filterNotesByDateRange(mentorId, start, end);
        List<NoteResponseDTO> dtoList = notes.stream()
                .map(NoteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/sorted/newest")
    public ResponseEntity<List<NoteResponseDTO>> getSortedNotesNewest(
            @RequestParam Long mentorId) {

        List<Note> notes = noteService.getSortedNotesByUploadDate(mentorId);
        List<NoteResponseDTO> dtoList = notes.stream()
                .map(NoteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/sorted/oldest")
    public ResponseEntity<List<NoteResponseDTO>> getSortedNotesOldest(
            @RequestParam Long mentorId) {

        List<Note> notes = noteService.getOldestNotes(mentorId);
        List<NoteResponseDTO> dtoList = notes.stream()
                .map(NoteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    // Delete a note by mentor
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<String> deleteNote(
            @PathVariable Long noticeId,
            @RequestParam Long mentorId) throws AccessDeniedException {

        noteService.deleteNote(noticeId,mentorId);
        return ResponseEntity.ok("Note deleted successfully");
    }
}
