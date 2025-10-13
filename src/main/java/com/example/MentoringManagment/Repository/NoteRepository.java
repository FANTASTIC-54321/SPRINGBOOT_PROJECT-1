package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findByMentor_UserId(Long mentorId);

    @Query("SELECT n FROM Note n WHERE " +
            "n.mentor.userId = :mentorId AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Note> searchNotesByKeyword(@Param("mentorId") Long mentorId, @Param("keyword") String keyword);

    @Query("SELECT n FROM Note n WHERE " +
            "n.mentor.userId = :mentorId AND " +
            "n.uploadDate BETWEEN :startDate AND :endDate")
    List<Note> filterNotesByDateRange(@Param("mentorId") Long mentorId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    List<Note> findByMentor_UserIdOrderByUploadDateDesc(Long mentorId);

    List<Note> findByMentor_UserIdOrderByUploadDateAsc(Long mentorId);
}
