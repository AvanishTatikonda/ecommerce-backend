package com.example.productservice5aug.repositories;

import com.example.productservice5aug.models.Category;
import com.example.productservice5aug.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Product save(Product p);// the attributes that are automatically generated
    // by db will not be in the param,but will be there
    //in the returned object


    List<Product> findAllByTitle(String title);

    List<Product> findByCreatedAtIsNotNullAndDescription(String description);

    List<Product> findByIdIs(Long id);
}
