package com.example.MentoringManagment.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "PDF file is required")
    private MultipartFile pdfFile;

}
