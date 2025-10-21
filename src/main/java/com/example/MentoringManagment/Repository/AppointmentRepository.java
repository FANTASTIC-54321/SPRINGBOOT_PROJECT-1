package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Appointment;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Request.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    boolean existsByMentorAndRequestedTimeAndStatus(User mentor, LocalDateTime time, AppointmentStatus status);

    List<Appointment> findByMentorAndRequestedTimeBetween(User mentor, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByStatusAndRequestedTimeBefore(AppointmentStatus status, LocalDateTime time);


    List<Appointment> findByMentorAndStatus(User mentor, AppointmentStatus status);

    List<Appointment> findByMentorAndRequestedTimeBeforeAndStatus(User mentor, LocalDateTime now, AppointmentStatus status);
}
