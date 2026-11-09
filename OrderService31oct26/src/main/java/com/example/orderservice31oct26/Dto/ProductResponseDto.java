package com.example.orderservice31oct26.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {

    private Long id;
    private String title;
    private Double price;
}