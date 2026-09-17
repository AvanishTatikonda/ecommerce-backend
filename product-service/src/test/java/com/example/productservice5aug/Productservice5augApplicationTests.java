package com.example.productservice5aug;

import com.example.productservice5aug.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class Productservice5augApplicationTests {
     @Autowired
    private ProductRepository productRepository;

     @Test
    void contextLoads(){

    }

    @Test
    void testingQueries() {
//        productRepository.findAllByTitle("hello");
        productRepository.findByIdIs(1L);
//      productRepository.findByCreatedAtIsNotNullAndDescription("hello");
    }

}
