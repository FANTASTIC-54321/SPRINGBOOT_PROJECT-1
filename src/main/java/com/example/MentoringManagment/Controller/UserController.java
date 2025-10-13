package com.example.MentoringManagment.Controller;


import com.example.MentoringManagment.DTO.*;
import com.example.MentoringManagment.Entity.User;
import com.example.MentoringManagment.Mapper.UserMapper;
import com.example.MentoringManagment.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register/mentor")
    public ResponseEntity<MentorResponseDTO> registerMentor(@Valid @RequestBody MentorRegisterDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerMentor(dto));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Mentor registration failed", e);
        }
    }

    @PostMapping("/register/mentee")
    public ResponseEntity<MenteeResponseDTO> registerMentee(@Valid @RequestBody MenteeRegisterDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerMentee(dto));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Mentee registration failed", e);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUser() {
        try {
            List<User> getUsers = userService.getAllUsers();
            return ResponseEntity.ok(getUsers);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch users", e);
        }
    }

    @GetMapping("/getAll/{id}")
    public ResponseEntity<UserDTO> getUserById(@Valid @PathVariable long id) {
        try {
            User user = userService.getUserById(id);
            UserDTO dto = UserMapper.toDTO(user);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist with this ID", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
        }
    }


//    @PutMapping("/updateUser/{id}")
//    public ResponseEntity<UserDTO> updateUserById(@PathVariable long id,@RequestBody UserDTO userDTO){
//        try{
//            UserDTO updatedUser = userService.updateUser(id, userDTO);
//            return ResponseEntity.ok(updatedUser);
//        }
//        catch (Exception e){
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
//        }
//
//    }

}
