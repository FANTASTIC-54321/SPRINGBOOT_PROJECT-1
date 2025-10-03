package com.example.MentoringManagment.Mapper;

import com.example.MentoringManagment.DTO.UserDTO;
import com.example.MentoringManagment.Entity.User;

public class UserMapper {

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole().toUpperCase());
        user.setDepartment(dto.getDepartment());
        user.setPhone(dto.getPhone());
        user.setSemester(dto.getSemester());
        return user;
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setDepartment(user.getDepartment());
        dto.setPhone(user.getPhone());
        dto.setSemester(user.getSemester());
        return dto;
    }
}

