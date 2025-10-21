package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.NoteRequestDTO;
import com.example.MentoringManagment.DTO.NotificationRequestDTO;
import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.Note;
import com.example.MentoringManagment.Entity.Notice;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import com.example.MentoringManagment.Repository.NoteRepository;
import com.example.MentoringManagment.Repository.NoticeRepository;
import com.example.MentoringManagment.Repository.UserRepository;
import com.example.MentoringManagment.Request.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {

    @Value("${note.storage.path}")
    private String storagePath;


    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MentorshipRepository mentorshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public Note createNote(Long mentorId, NoteRequestDTO request) throws IOException {
        System.out.println("Received Notice upload: title=" + request.getTitle() +
                ", file=" + (request.getPdfFile() != null ? request.getPdfFile().getOriginalFilename() : "null"));

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Mentor Not Found"));

        Mentorship mentorship = mentorshipRepository.findByStudent_UserId(mentor.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentorship not found"));

        if(!"MENTOR".equalsIgnoreCase(mentor.getRole())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User must be a mentor");
        }

        MultipartFile pdfFile = request.getPdfFile();

        if (pdfFile.getSize() < 10 * 1024) {  // size() returns bytes
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size must be at least 10 KB");
        }

        if (!"application.pdf".equals(pdfFile.getContentType())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF files allowed");
        }

        String originalFileName = Paths.get(pdfFile.getOriginalFilename()).getFileName().toString();
        String fileName = UUID.randomUUID()+"_"+ originalFileName;
        Path filePath = Paths.get(storagePath, fileName);
        Files.write(filePath, pdfFile.getBytes());

        Note note = new Note();
        note.setMentor(mentor);
        note.setDescription(request.getDescription());
        note.setTitle(request.getTitle());
        note.setUploadDate(LocalDate.now());
        note.setFileUrl(fileName);

        try{
            Note savedNote = noteRepository.save(note);
            System.out.println("Note saved successfully with ID: " + savedNote.getId());

            NotificationRequestDTO dto = new NotificationRequestDTO();
            dto.setReceiverId(mentorship.getStudent().getUserId());
            dto.setType(NotificationType.NOTE);
            dto.setMessage("New note uploaded: " + request.getTitle());
            notificationService.createNotification(dto);

            return savedNote;
        }
        catch (Exception e){
            System.err.println("Failed to save Note: " + e.getMessage());
            e.printStackTrace();  // Print full stack trace for debugging
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save note: " + e.getMessage());
        }


    }

    // Get all notices for a mentee (all notes from their mentor)
    public List<Note> getNoticesForMentee(Long menteeId) {
        Mentorship mentorship = mentorshipRepository.findByStudent_UserId(menteeId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,("Mentorship not found for mentee")));

        Long mentorId = mentorship.getMentor().getUserId();
        return noteRepository.findByMentor_UserId(mentorId);
    }

    // Mentor deletes notice if it belongs to them
    public void deleteNote(Long noteId, Long mentorId){
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Note not found"));

        if (!note.getMentor().getUserId().equals(mentorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Not authorized to delete this notice");
        }
        try {
            Path filePath = Paths.get(storagePath, note.getFileUrl());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        noteRepository.delete(note);
    }

    public Note getNoteById(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Note not found"));
    }

    public Resource loadFileAsResource(Path fileName) throws IOException {
        Path filePath = Paths.get(storagePath, String.valueOf(fileName));
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read file: " + filePath);
        }
    }

    public List<Note> searchNotesByKeyword(Long mentorId, String keyword) {
        return noteRepository.searchNotesByKeyword(mentorId, keyword);
    }

    public List<Note> filterNotesByDateRange(Long mentorId, LocalDate startDate, LocalDate endDate) {
        return noteRepository.filterNotesByDateRange(mentorId, startDate, endDate);
    }

    public List<Note> getSortedNotesByUploadDate(Long mentorId) {
        return noteRepository.findByMentor_UserIdOrderByUploadDateDesc(mentorId);
    }

    public List<Note> getOldestNotes(Long mentorId) {
        return noteRepository.findByMentor_UserIdOrderByUploadDateAsc(mentorId);
    }

}
