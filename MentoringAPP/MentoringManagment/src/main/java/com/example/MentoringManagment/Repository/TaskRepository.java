package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks,Long> {

    // To find all tasks associated with a mentor
    List<Tasks> findByMentorshipId(Long mentorshipId);

}
