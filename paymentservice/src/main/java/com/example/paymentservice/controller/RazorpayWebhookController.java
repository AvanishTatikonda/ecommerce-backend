package com.example.paymentservice.controller;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.model.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import com.razorpay.Utils;


@RestController
@RequestMapping("/webhook")
public class RazorpayWebhookController {

    private final PaymentRepository paymentRepository;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    public RazorpayWebhookController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/razorpay")
    public String handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            boolean isValid = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret
            );

            if (!isValid) {
                return "Invalid webhook signature";
            }

        } catch (Exception e) {
            return "Webhook verification failed";
        }
        System.out.println("Razorpay Webhook Received:");
        System.out.println(payload);

        JSONObject json = new JSONObject(payload);
        String event = json.getString("event");
        if (event.startsWith("refund.")) {
            JSONObject refundEntity =
                    json.getJSONObject("payload")
                            .getJSONObject("refund")
                            .getJSONObject("entity");

            String refundId = refundEntity.getString("id");
            String paymentId = refundEntity.getString("payment_id");
            Long refundAmount = refundEntity.getLong("amount");
            String refundStatus = refundEntity.getString("status");

            Payment payment = paymentRepository
                    .findByRazorpayPaymentId(paymentId)
                    .orElse(null);

            if (payment == null) {
                return "Payment not found";
            }
            if (event.equals("refund.processed")) {
                payment.setStatus(PaymentStatus.REFUNDED);
                paymentRepository.save(payment);
            }

            System.out.println("Refund ID: " + refundId);
            System.out.println("Payment ID: " + paymentId);
            System.out.println("Refund Amount: " + refundAmount);
            System.out.println("Refund Status: " + refundStatus);

            return "Refund webhook received";
        }
        JSONObject paymentEntity =
                json.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String paymentId = paymentEntity.getString("id");
        String orderId = paymentEntity.getString("order_id");
        Long amount = paymentEntity.getLong("amount");
        String currency = paymentEntity.getString("currency");
//        String status = paymentEntity.getString("status");
        String method = paymentEntity.getString("method");
        if (paymentRepository.findByRazorpayPaymentId(paymentId).isPresent()) {
            return "Payment already processed";
        }
        Payment payment = new Payment();

        payment.setRazorpayPaymentId(paymentId);
        payment.setRazorpayOrderId(orderId);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setPaymentMethod(method);

        if (event.equals("payment.captured")) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else if (event.equals("payment.failed")) {
            payment.setStatus(PaymentStatus.FAILURE);
        }

        paymentRepository.save(payment);

        return "Webhook received";
    }
}