package com.example.productservice5aug.repositories;

import com.example.productservice5aug.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByTitle(String title);
}
