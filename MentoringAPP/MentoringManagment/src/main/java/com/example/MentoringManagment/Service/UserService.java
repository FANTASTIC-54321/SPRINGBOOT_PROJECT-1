package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.UserDTO;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.UserMapper;
import com.example.MentoringManagment.Repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO registerUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByEmail(dto.getEmail())) {
            throw new EntityExistsException("User Already Exist");
        }

        User userEntity = UserMapper.toEntity(dto);
        User savedEntity = userRepository.save(userEntity);
        return UserMapper.toDTO(savedEntity);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

//    public UserDTO updateUser(Long userId, UserUpdateDTO dto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserMapper.toEntity(user);
//        return userRepository.save(user);
//    }



}
