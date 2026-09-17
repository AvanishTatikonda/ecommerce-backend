package com.example.userservice.Repository;

import com.example.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    User save(User user);
    Optional<User> findByEmail(String email);
}
