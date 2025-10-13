package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

//    boolean existsByUsernameIgnoreCase(String username);
//    boolean existsByEmailIgnoreCase(String email);


    boolean existsByUsernameOrEmailOrPhone(String username, String email, Long phone);

    boolean existsByPhone(Long phone);


//    @Override
//    boolean existsById(Long userId);
}
