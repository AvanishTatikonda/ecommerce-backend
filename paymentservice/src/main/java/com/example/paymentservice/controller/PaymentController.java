package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentOrderResponse;
import com.example.paymentservice.service.RazorpayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final RazorpayService razorpayService;

    public PaymentController(RazorpayService razorpayService) {
        this.razorpayService = razorpayService;
    }

//    @PostMapping("/create-order")
//    public PaymentOrderResponse createOrder() throws Exception {
//        return razorpayService.createOrder();
//    }

    @PostMapping("/create-payment-link")
    public String createPaymentLink() throws Exception {
        return razorpayService.createPaymentLink();
    }
}