package com.example.orderservice31oct26.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CartResponseDto {

    private Long id;
    private Long userId;
    @JsonProperty("cartitems")
    private List<CartItemResponseDto> cartItems;
}