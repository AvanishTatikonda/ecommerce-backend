package com.example.productservice5aug.services;

import com.example.productservice5aug.dtos.CreateProductRequestDto;
import com.example.productservice5aug.models.Product;

import java.util.List;

public interface ProductService {

   Product createProduct(Product product);
    Product getSingleProduct(Long id);

   // Product createProduct(String title, String description, String category, double price, String image);

    List<Product> getAllProducts();
    Product updateProduct(Long id,Product product);
    void deleteProduct(Long id);
}
