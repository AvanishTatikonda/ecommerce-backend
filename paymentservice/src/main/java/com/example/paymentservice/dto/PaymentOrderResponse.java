package com.example.paymentservice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentOrderResponse {

    private String orderId;
    private int amount;
    private String currency;
    private String keyId;

}