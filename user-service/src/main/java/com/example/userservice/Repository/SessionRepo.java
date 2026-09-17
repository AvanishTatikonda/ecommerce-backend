package com.example.userservice.Repository;

import com.example.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<Session,Long> {
    Session save(Session session);
}
