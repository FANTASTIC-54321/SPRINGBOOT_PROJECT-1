package com.example.MentoringManagment.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorship_id", nullable = false)
    private Mentorship mentorship;

    @Column(nullable = false)
    private String title;

    @Lob    // Use it when you need to store large amounts of text or binary content in the database (e.g., documents, images, big logs, PDFs).
    private String description;

    @Column(nullable = false)
    private LocalDate assignedDate = LocalDate.now();

    private LocalDate dueDate;

    @Column(nullable = false)
    private String status = "PENDING"; // Possible values: PENDING, IN_PROGRESS, COMPLETED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigner_id", nullable = false)
    private User assignedBy;

}
