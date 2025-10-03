package com.example.MentoringManagment.Controller;


import com.example.MentoringManagment.DTO.TaskDTO;
import com.example.MentoringManagment.Entity.Tasks;
import com.example.MentoringManagment.Request.UpdateStatusRequest;
import com.example.MentoringManagment.Service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/createTask")
    // Valid :- Spring to automatically validate the fields of that DTO before the method executes.
    public ResponseEntity<TaskDTO> createTask( @Valid @RequestBody TaskDTO taskDTO){

        TaskDTO taskReq = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskReq);

    }

    @GetMapping("/tasks/{mentorshipId}")
    public ResponseEntity<List<Tasks>> getTasksByMentorshipId(@PathVariable Long mentorshipId){

        List<Tasks> allTasks = taskService.getTasksByMentorshipId(mentorshipId);
        return ResponseEntity.ok(allTasks);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable long taskId, @Valid @RequestBody UpdateStatusRequest request){
        // We can use Map<String, String> in parameter, but I don't know how it is being used.
        String newStatus = request.getStatus();
        TaskDTO updatedTask = taskService.updateTaskStatus(taskId,newStatus);

        return ResponseEntity.ok(updatedTask);
    }

                                                  @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable long taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body("Task Deleted successfully");
    }


}
