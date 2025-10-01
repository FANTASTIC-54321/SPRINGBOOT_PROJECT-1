package com.example.MentoringManagment.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "mentorship")
@Data
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
public class Mentorship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @OneToOne
    @JoinColumn(name = "student_id",unique = true)
    private User student;

    private LocalDate mentorshipStartDate;

    private LocalDate mentorshipEndDate;

    private boolean active ;

}
