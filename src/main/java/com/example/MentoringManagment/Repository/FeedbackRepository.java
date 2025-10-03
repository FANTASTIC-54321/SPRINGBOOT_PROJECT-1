package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedBack,Long> {

    List<FeedBack> findByMentorship_Student_UserId(Long userId);
    List<FeedBack> findByTask_Id(Long taskId);
    List<FeedBack> findByRating(Integer rating);
    List<FeedBack> findByMentorship_Id(Long userId);


}




