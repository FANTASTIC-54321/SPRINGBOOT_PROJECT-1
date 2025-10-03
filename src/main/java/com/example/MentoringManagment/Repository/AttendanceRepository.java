package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

//    List<Attendance> findByMentorship_Id(Long mentorshipID);

    Optional<Attendance> findByMentorship_IdAndSessionDate(Long mentorshipId, LocalDate sessionDate);

    // JPA query methods use entity property names, not database column names
    List<Attendance> findByMentorship_IdOrderBySessionDateDesc(Long mentorshipId);


}

// Here’s the rule: any time you filter by a nested entity’s field you must use the property
// path syntax in your Spring-Data method name

// There is no mentorshipId field on Attendance—only a mentorship reference whose .id you want to compare.
// That means every method that currently reads findByMentorshipId… needs to become findByMentorship_Id….



