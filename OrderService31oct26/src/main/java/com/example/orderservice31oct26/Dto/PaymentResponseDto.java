package com.example.orderservice31oct26.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {
    private String razorpayOrderId;
    private int amount;
    private String currency;
    private String keyId;
}
