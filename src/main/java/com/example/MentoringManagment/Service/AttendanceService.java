package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.AttendanceDTO;
import com.example.MentoringManagment.DTO.AttendancePatchDTO;
import com.example.MentoringManagment.DTO.AttendanceResponseDTO;
import com.example.MentoringManagment.Entity.Attendance;
import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.GETAttendanceMapper;
import com.example.MentoringManagment.Repository.AttendanceRepository;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import com.example.MentoringManagment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MentorshipRepository mentorshipRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private GETAttendanceMapper getAttendanceMapper;
    // Your GETAttendanceMapper is a plain class, but you're autowiring it in the service.
    // To make this work, you need to annotate the mapper class with @Component

    public Attendance markAttendance(AttendanceDTO request){

        User user   = userRepository.findById(request.getUserId())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found"));

        Mentorship mentorship = mentorshipRepository.findById(request.getMentorshipId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Mentorship not found"));

         if (!"MENTOR".equalsIgnoreCase(user.getRole())){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only mentors are allowed to mark attendance");
         }

        if (!mentorshipRepository.existsById(request.getMentorshipId())) {
            throw new RuntimeException("Mentorship ID doesn't exist");
        }

        if (attendanceRepository.findByMentorship_IdAndSessionDate(request.getMentorshipId(), request.getSessionDate()).isPresent()) {
            throw new RuntimeException("Attendance already recorded for this session");
        }

        Attendance attendance = new Attendance();
        attendance.setMentorship(mentorship);
        attendance.setSessionDate(request.getSessionDate());
        attendance.setPresent(request.getIsPresent());

        return attendanceRepository.save(attendance);


    }

    public List<AttendanceResponseDTO> getAttendanceByMentorshipId(Long mentorshipId) {

        if (!mentorshipRepository.existsById(mentorshipId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Mentorship not found with ID: " + mentorshipId);
        }
        // Use the repository method to fetch and order by date
        List<Attendance> attendanceList = attendanceRepository.findByMentorship_IdOrderBySessionDateDesc(mentorshipId);

        List<AttendanceResponseDTO> dtoList = new ArrayList<>();
        for (Attendance att : attendanceList) {
            dtoList.add(GETAttendanceMapper.toDTO(att));
        }
        return dtoList;
    }

    public Attendance updateAttendance(Long attendanceId , AttendancePatchDTO request){
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Attendance record not found with ID: " + attendanceId));

        User user   = userRepository.findById(request.getUserId())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found"));

        if(!"MENTOR".equalsIgnoreCase(user.getRole())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only mentors are allowed to Update attendance");
        }

        if (request.getSessionDate() != null){
            attendance.setSessionDate(request.getSessionDate());
        }
        if(request.getIsPresent()!=null){
            attendance.setPresent(request.getIsPresent());
        }

        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long attendanceId){

        if(!attendanceRepository.existsById(attendanceId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Attendance record not found with ID: " + attendanceId);
        }
        attendanceRepository.deleteById(attendanceId);
    }
}
