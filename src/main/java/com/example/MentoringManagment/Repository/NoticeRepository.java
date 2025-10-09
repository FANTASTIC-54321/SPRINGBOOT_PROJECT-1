package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.Notice;
import com.example.MentoringManagment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByMentor_UserId(Long mentorId);

}
