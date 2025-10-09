package com.example.MentoringManagment.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private LocalDateTime uploadDate;


}


///  If you store mentor’s name in both the User table and the Notice table:
///     Mentor’s name is duplicated in multiple places.
///     If the mentor changes their name, you must update it in all related Notice records.
///     If you miss updating any Notice record, data becomes inconsistent (one place old name, another place new name).
