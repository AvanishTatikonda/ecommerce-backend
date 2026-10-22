package com.example.paymentservice.Config;

import com.razorpay.RazorpayClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfiguration {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @PostConstruct
    public void checkCredentials() {
        System.out.println("KEY ID = " + keyId);
        System.out.println("SECRET PRESENT = " +
                (keySecret != null && !keySecret.isEmpty()));
    }

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        return new RazorpayClient(keyId, keySecret);
    }
}