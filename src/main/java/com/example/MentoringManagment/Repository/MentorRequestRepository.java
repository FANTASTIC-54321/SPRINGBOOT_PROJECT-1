package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.MentorRequest;
import com.example.MentoringManagment.Request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRequestRepository extends JpaRepository<MentorRequest, Long> {

    Optional<MentorRequest> findByUser_UserId(Long userId);

    List<MentorRequest> findByStatus(RequestStatus status);
}
