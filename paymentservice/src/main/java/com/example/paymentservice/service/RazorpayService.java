package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentOrderResponse;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String keyId;

    public RazorpayService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    // Existing createOrder() method
    // ...

    // New method
    public String createPaymentLink() throws Exception {

        JSONObject paymentLinkRequest = new JSONObject();

        paymentLinkRequest.put("amount", 200000);
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("description", "Test Payment");

        var paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

        return paymentLink.get("short_url");
    }
}