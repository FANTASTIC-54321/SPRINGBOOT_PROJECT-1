package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.NoticeRequestDTO;
import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.Notice;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import com.example.MentoringManagment.Repository.NoticeRepository;
import com.example.MentoringManagment.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    @Value("${notice.storage.path}")
    private String storagePath;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private MentorshipRepository mentorshipRepository;

    @Autowired
    private UserRepository userRepository;

    public Notice createNotice(Long mentorId, NoticeRequestDTO request) throws IOException {
        // Find mentor user from DB (reuse the User entity)
        System.out.println("Received Notice upload: title=" + request.getTitle() +
                ", file=" + (request.getPdfFile() != null ? request.getPdfFile().getOriginalFilename() : "null"));

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        if (!"MENTOR".equalsIgnoreCase(mentor.getRole())){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN,"User must be a mentor");
        }

        MultipartFile pdfFile = request.getPdfFile();

        // Minimum file size (10 KB) check
        if (pdfFile.getSize() < 10 * 1024) {  // size() returns bytes
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size must be at least 10 KB");
        }

        if (!"application/pdf".equals(pdfFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF files allowed");
        }

        // Use storagePath injected from application.properties for saving files
        String originalFileName = Paths.get(pdfFile.getOriginalFilename()).getFileName().toString();  // sanitize filename
        String fileName = UUID.randomUUID() + "_" + originalFileName;
        Path filePath = Paths.get(storagePath, fileName);  // use the configured folder
        Files.createDirectories(filePath.getParent());     // create parent folders if missing
        Files.write(filePath, pdfFile.getBytes());         // write the PDF bytes to disk

        // Build notice entity
        Notice notice = new Notice();
        notice.setMentor(mentor);               // set the existing mentor entity
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        notice.setFileUrl(fileName);  // safer and portable // store relative path as needed, when you need to access the file, combine storagePath + fileUrl
        notice.setUploadDate(LocalDateTime.now());

//        return noticeRepository.save(notice);

        try {
            Notice savedNotice = noticeRepository.save(notice);
            System.out.println("Notice saved successfully with ID: " + savedNotice.getId());
            return savedNotice;
        } catch (Exception e) {
            System.err.println("Failed to save Notice: " + e.getMessage());
            e.printStackTrace();  // Print full stack trace for debugging
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save notice: " + e.getMessage());
        }
    }

    // Get all notices for a mentee (all notices from their mentor)
    public List<Notice> getNoticesForMentee(Long menteeId) {
        Mentorship mentorship = mentorshipRepository.findByStudent_UserId(menteeId)
                .orElseThrow(() -> new RuntimeException("Mentorship not found for mentee"));

        Long mentorId = mentorship.getMentor().getUserId();

        return noticeRepository.findByMentor_UserId(mentorId);
    }


    // Mentor deletes notice if it belongs to them
    public void deleteNotice(Long noticeId, Long mentorId) throws AccessDeniedException {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        if (!notice.getMentor().getUserId().equals(mentorId)) {
            throw new AccessDeniedException("Not authorized to delete this notice");
        }

        // Delete file on disk
        try {
            Path filePath = Paths.get(storagePath, notice.getFileUrl());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        noticeRepository.delete(notice);
    }

    public Notice getNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
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




    ///  Imagine a website where many users upload files named exactly the same—like resume.pdf or project.pdf.
    ///
    /// If you just save files by original names, everytime two users upload a file with the name resume.pdf, the previous file would get overwritten.
    /// How UUID solves this:
    ///
    /// Instead of saving resume.pdf you save it as:
    ///
    ///
    /// d290f1ee-6c54-4b01-90e6-d701748f0851_resume.pdf
    ///
    /// If another user uploads their resume.pdf, it will be saved as:
    ///
    /// 9b2c5ee9-23be-4e3f-a1b7-9ac0c1e5cd32_resume.pdf
    ///
    /// Both files coexist safely because the UUID prefix is guaranteed to be unique, avoiding filename collisions


}
