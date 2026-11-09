package com.example.orderservice31oct26.repository;

import com.example.orderservice31oct26.model.Orderitem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<Orderitem,Long> {
    
}
