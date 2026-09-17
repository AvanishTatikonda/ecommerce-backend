package com.example.productservice5aug.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreCreateProductRequestDto {
    private  String title;
    private double price;
    private String image;
    private String description;
    private String category;
}
