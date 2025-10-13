package com.example.MentoringManagment.Service;

import com.example.MentoringManagment.DTO.*;
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

//    @Autowired
//    private PasswordEncoder passwordEncoder;


//    public UserDTO registerUser(UserDTO dto) {
//        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByEmail(dto.getEmail())) {
//            throw new EntityExistsException("User Already Exist");
//        }
//
//        User userEntity = UserMapper.toEntity(dto);
//        User savedEntity = userRepository.save(userEntity);
//        return UserMapper.toDTO(savedEntity);
//    }

    public MenteeResponseDTO registerMentee(MenteeRegisterDTO dto) {
        validateUniqueness(dto.getUsername(), dto.getEmail(), dto.getPhone());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setDepartment(dto.getDepartment());
        user.setRole("MENTEE");
        user.setPhone(dto.getPhone());
        user.setSemester(dto.getSemester());

        return UserMapper.toResponseDTO(userRepository.save(user));
    }


    public MentorResponseDTO registerMentor(MentorRegisterDTO dto) {
        validateUniqueness(dto.getUsername(), dto.getEmail(), dto.getPhone());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setDepartment(dto.getDepartment());
        user.setRole("MENTOR");
        user.setPhone(dto.getPhone());
        user.setSemester(null);

        return UserMapper.toMentorResponseDTO(userRepository.save(user));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    private void validateUniqueness(String username, String email, Long phone) {
        if (userRepository.existsByUsernameOrEmailOrPhone(username, email, phone)) {
            throw new EntityExistsException("User already exists with same username, email, or phone");
        }
    }

//    public UserDTO updateUser(Long userId, UserUpdateDTO dto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserMapper.toEntity(user);
//        return userRepository.save(user);
//    }



}
