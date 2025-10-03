package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorshipRepository extends JpaRepository<Mentorship,Long> {
    List<Mentorship> findByMentorUserId(Long mentorId);
    List<Mentorship> findByStudentUserId(Long studentId);
}
