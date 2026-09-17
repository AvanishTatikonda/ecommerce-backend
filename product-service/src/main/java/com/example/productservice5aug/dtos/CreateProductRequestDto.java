package com.example.productservice5aug.dtos;

import com.example.productservice5aug.models.Category;
import com.example.productservice5aug.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String imageUrl;
    private String categoryName;

    public Product toProduct(){
        Product product =new Product();
        product.setId(this.id);
        product.setTitle(this.title);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setImageUrl(this.imageUrl);
        Category category = new Category();
        category.setTitle(this.categoryName);
        product.setCategory(category);

        return product;
    }
}
