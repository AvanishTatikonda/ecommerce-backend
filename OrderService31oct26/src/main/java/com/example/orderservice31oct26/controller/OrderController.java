package com.example.orderservice31oct26.controller;

import com.example.orderservice31oct26.Dto.*;
import com.example.orderservice31oct26.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final  OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody CreateOrderRequest request){
        OrderResponseDto response=orderService.createOrder(request.getUserId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrderByUserId(
            @PathVariable Long userId){
        List<OrderResponseDto> orders=orderService.getOrderByUserId(userId);

        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(
            @PathVariable Long orderId){
          OrderResponseDto response= orderService.getOrderById(orderId);
          return ResponseEntity.ok(response);
    }
    @PutMapping("/{orderId}/status")
    public  ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequestDto request) {

        OrderResponseDto response = orderService.updateOrderStatus(
                orderId,
                request.getStatus()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<PaymentResponseDto> initialPayment(
            @PathVariable Long orderId){
        PaymentResponseDto response=orderService.initialPayment(orderId);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/{orderId}/verify-payment")
    public ResponseEntity<Boolean> verifyPayment(
            @PathVariable Long orderId,
            @RequestBody VerifyPaymentRequest request) {

        request.setOrderId(orderId);

        boolean verified = orderService.verifyPayment(request);

        return ResponseEntity.ok(verified);
    }
    @PostMapping("/{orderId}/payment-link")
    public ResponseEntity<CreatePaymentLinkResponse> initiatePaymentLink(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.initiatePaymentLink(orderId)
        );
    }
    @PostMapping("/{orderId}/verify-payment-link")
    public ResponseEntity<Boolean> verifyPaymentLink(
            @PathVariable Long orderId) {

        boolean verified = orderService.verifyPaymentLink(orderId);

        return ResponseEntity.ok(verified);
    }
}
