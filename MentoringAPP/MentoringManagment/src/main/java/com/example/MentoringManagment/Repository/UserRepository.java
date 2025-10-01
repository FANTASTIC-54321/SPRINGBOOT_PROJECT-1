package com.example.MentoringManagment.Repository;

import com.example.MentoringManagment.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

//    @Override
//    boolean existsById(Long userId);
}
