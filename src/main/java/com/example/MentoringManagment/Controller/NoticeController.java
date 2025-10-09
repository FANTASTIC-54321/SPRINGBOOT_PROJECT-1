package com.example.MentoringManagment.Controller;

import com.example.MentoringManagment.DTO.NoticeRequestDTO;
import com.example.MentoringManagment.DTO.NoticeResponseDTO;
import com.example.MentoringManagment.Entity.Notice;
import com.example.MentoringManagment.Mapper.NoticeMapper;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import com.example.MentoringManagment.Service.NoticeService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.example.MentoringManagment.Mapper.NoticeMapper.toDTO;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MentorshipRepository mentorshipRepository;


    // Upload a new notice by mentor
    @PostMapping("/upload")
    public ResponseEntity<NoticeResponseDTO> uploadNotice(
            @RequestParam Long mentorId,
            @Valid @ModelAttribute NoticeRequestDTO requestDTO) throws IOException {

        Notice notice = noticeService.createNotice(mentorId, requestDTO);

        NoticeResponseDTO responseDTO = toDTO(notice);
        return ResponseEntity.ok(responseDTO);
    }

    // Get all notices accessible to a mentee
    @GetMapping("/mentee/{menteeId}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesForMentee(@PathVariable Long menteeId) {
        List<Notice> notices = noticeService.getNoticesForMentee(menteeId);
        List<NoticeResponseDTO> dtos = notices.stream()
                .map(NoticeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Download the notice PDF file by noticeId
    @GetMapping("/download/{noticeId}")
    public ResponseEntity<Resource> downloadNoticeFile(
            @PathVariable Long noticeId,
            @RequestParam Long userId) throws IOException {

        // Check if userId is mentor or mentee linked to this notice
        Notice notice = noticeService.getNoticeById(noticeId);
        Long mentorId = notice.getMentor().getUserId();
        boolean authorized = false;

        // To check if the User is Mentor
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
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN,"User not authorized to access the notice");
        }

        Path filePath = Paths.get(notice.getFileUrl());
        Resource resource =  noticeService.loadFileAsResource(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(resource);
    }

    // Delete a notice by mentor
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<String> deleteNotice(
            @PathVariable Long noticeId,
            @RequestParam Long mentorId) throws AccessDeniedException {
        noticeService.deleteNotice(noticeId, mentorId);
        return ResponseEntity.ok("Notice deleted successfully");
    }
}
