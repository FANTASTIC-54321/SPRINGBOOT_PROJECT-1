package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.TaskDTO;
import com.example.MentoringManagment.Entity.Tasks;


public class TaskMapper {

    public static TaskDTO convertToDto (Tasks task){
        TaskDTO dto = new TaskDTO();

        dto.setMentorshipId(task.getMentorship().getId());
        dto.setAssignerId(task.getAssignedBy().getUserId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        return dto;
    }
}
