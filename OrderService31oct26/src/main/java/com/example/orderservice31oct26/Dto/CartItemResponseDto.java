package com.example.orderservice31oct26.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDto {

    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
}