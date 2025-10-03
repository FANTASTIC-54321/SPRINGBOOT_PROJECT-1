package com.example.MentoringManagment.Service;


import com.example.MentoringManagment.DTO.TaskDTO;
import com.example.MentoringManagment.Entity.Mentorship;
import com.example.MentoringManagment.Entity.Tasks;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.TaskMapper;
import com.example.MentoringManagment.Repository.MentorshipRepository;
import com.example.MentoringManagment.Repository.TaskRepository;
import com.example.MentoringManagment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.MentoringManagment.Mapper.TaskMapper.convertToDto;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MentorshipRepository mentorshipRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskDTO createTask(TaskDTO request){

        Mentorship mentorship = mentorshipRepository.findById(request.getMentorshipId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentorship not found with ID: " + request.getMentorshipId()));

        User assigner = userRepository.findById(request.getAssignerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User (Assigner) not found with ID: " + request.getAssignerId()));


        Tasks task = new Tasks();

        task.setMentorship(mentorship);
        task.setAssignedBy(assigner);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        task.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");

        Tasks saved =  taskRepository.save(task);

        return convertToDto(saved);

    }

    public List<Tasks> getTasksByMentorshipId(Long mentorshipId){

        if(!mentorshipRepository.existsById(mentorshipId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,("Mentorship not found with ID: " + mentorshipId));
        }

        return taskRepository.findByMentorshipId(mentorshipId);
    }

    public TaskDTO updateTaskStatus(Long taskId, String newStatus){
        Tasks task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        if(!List.of("PENDING", "IN_PROGRESS", "COMPLETED").contains(newStatus.toUpperCase())){
            throw new IllegalArgumentException("Invalid status provided. Must be PENDING, IN_PROGRESS, or COMPLETED.");
        }

        Tasks savedTask = taskRepository.save(task);
        return TaskMapper.convertToDto(savedTask);

    }

    public void deleteTask(Long taskId){
        if(!taskRepository.existsById(taskId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found with ID: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

}
