package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorshipRepository extends JpaRepository<Mentorship,Long> {
    List<Mentorship> findByMentor_UserId(Long mentorId);
    Optional<Mentorship> findByStudent_UserId(Long studentId);
    Optional<Mentorship> findByMentor_UserIdAndStudent_UserId(Long mentorId, Long studentId);
}
