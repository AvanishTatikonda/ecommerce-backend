package com.example.orderservice31oct26.Dto;

import com.example.orderservice31oct26.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequestDto {
    private OrderStatus status;
}
