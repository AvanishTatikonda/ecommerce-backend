package com.example.cartservice30oct26.repository;

import com.example.cartservice30oct26.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByuserId(Long userId);
}
