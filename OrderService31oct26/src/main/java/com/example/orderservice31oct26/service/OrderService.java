package com.example.orderservice31oct26.service;

import com.example.orderservice31oct26.Dto.*;
import com.example.orderservice31oct26.enums.OrderStatus;
import com.example.orderservice31oct26.exception.CartEmptyException;
import com.example.orderservice31oct26.exception.OrderNotFoundException;
import com.example.orderservice31oct26.model.Order;
import com.example.orderservice31oct26.model.Orderitem;
import com.example.orderservice31oct26.repository.OrderRepository;
import com.example.orderservice31oct26.Dto.PaymentRequestDto;
import com.example.orderservice31oct26.Dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.orderservice31oct26.Dto.VerifyPaymentRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(
            RestTemplate restTemplate,
            OrderRepository orderRepository){
        this.orderRepository=orderRepository;
        this.restTemplate=restTemplate;
    }
    public CartResponseDto getCart(Long userId) {

        String url = "http://CartService30oct26/cart/{userId}";

        return restTemplate.getForObject(
                url,
                CartResponseDto.class,
                userId
        );
    }
    public ProductResponseDto getProduct(Long productId){
        String url="http://productservice/products/{id}";
        return restTemplate.getForObject(
                url,
                ProductResponseDto.class,
                productId
        );
    }
    private OrderResponseDto convertToDto(Order order) {

        OrderResponseDto response = new OrderResponseDto();

        response.setOrderId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponseDto> items = new ArrayList<>();

        for (Orderitem item : order.getOrderitems()) {

            OrderItemResponseDto itemDto = new OrderItemResponseDto();

            itemDto.setProductId(item.getProductId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());

            items.add(itemDto);
        }
        response.setOrderitems(items);

        return response;
    }
    public OrderResponseDto createOrder(Long userId) {
        //now we are fetching cart
        CartResponseDto cart = getCart(userId);
        //checking cart is empty or not
        if (cart == null || cart.getCartItems() == null ||
                cart.getCartItems().isEmpty()) {

            throw new CartEmptyException("Cart is empty");
        }
       //create order obeject
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        //Intialize total and order items
        double totalAmount = 0.0;
        List<Orderitem> orderItems = new ArrayList<>();
        //Fetch products, calculate total and create OrderItems
        for (CartItemResponseDto cartItem : cart.getCartItems()) {

            ProductResponseDto product = getProduct(cartItem.getProductId());

            totalAmount += product.getPrice() * cartItem.getQuantity();
            Orderitem orderItem = new Orderitem();

            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }
        //Attach calculated values
        order.setTotalAmount(totalAmount);
       order.setOrderitems(orderItems);
       //save order
        Order savedOrder = orderRepository.save(order);
        //return response
        return convertToDto(savedOrder);
    }
    public List<OrderResponseDto> getOrderByUserId(Long userId){
        List<Order> orders=orderRepository.findByUserId(userId);
        List<OrderResponseDto> response=new ArrayList<>();

        for(Order order:orders){
            response.add(convertToDto(order));
        }
        return response;
    }
    public OrderResponseDto getOrderById(Long orderId){
        Order order =orderRepository.findById(orderId)
                .orElseThrow(()->
                        new OrderNotFoundException("order not found :" + orderId));
                return convertToDto(order);

    }

    //update order status
    public OrderResponseDto updateOrderStatus(
            Long orderId, OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }

        if (status != OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Only cancellation is allowed through this endpoint"
            );
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending orders can be cancelled"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order updatedOrder = orderRepository.save(order);

        return convertToDto(updatedOrder);
    }
    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    //intiate payment

    public PaymentResponseDto initialPayment(Long orderId){

        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->
                new OrderNotFoundException("Order not found:" + orderId));
        if(order.getStatus()!=OrderStatus.PENDING){
            throw new IllegalStateException("Payment can only be intiated for pending orders");
        }
        int amountInPaise = (int) Math.round(order.getTotalAmount() * 100);

        PaymentRequestDto request = new PaymentRequestDto();
        request.setOrderId(order.getId());
        request.setAmount(amountInPaise);

        PaymentResponseDto response = restTemplate.postForObject(
                paymentServiceUrl + "/payments/create-order",
                request,
                PaymentResponseDto.class
        );
        if (response == null) {
            throw new IllegalStateException("Payment service returned empty response");
        }

        order.setRazorpayOrderId(response.getRazorpayOrderId());

        orderRepository.save(order);

        return response;
    }
    //verify payment
    public boolean verifyPayment(VerifyPaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found: " + request.getOrderId()
                        )
                );

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment can only be verified for pending orders"
            );
        }

        if (order.getRazorpayOrderId() == null ||
                !order.getRazorpayOrderId()
                        .equals(request.getRazorpayOrderId())) {
            throw new IllegalArgumentException(
                    "Razorpay order ID does not match"
            );
        }

        String url = paymentServiceUrl + "/payments/verify";

        Boolean verified = restTemplate.postForObject(
                url,
                request,
                Boolean.class
        );

        if (!Boolean.TRUE.equals(verified)) {
            return false;
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return true;
    }
    // verifypaymentlink
    
    public boolean verifyPaymentLink(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment can only be verified for pending orders");
        }

        if (order.getRazorpayPaymentLinkId() == null) {
            throw new IllegalStateException("Payment Link ID not found");
        }

        VerifyPaymentRequest linkRequest = new VerifyPaymentRequest();
        linkRequest.setPaymentLinkId(order.getRazorpayPaymentLinkId());

        int expectedAmount = (int) Math.round(order.getTotalAmount() * 100);
        linkRequest.setExpectedAmount(expectedAmount);

        Boolean verified = restTemplate.postForObject(
                paymentServiceUrl + "/payments/verify-payment-link",
                linkRequest,
                Boolean.class
        );

        if (!Boolean.TRUE.equals(verified)) {
            return false;
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return true;
    }

    //payment link
    public CreatePaymentLinkResponse initiatePaymentLink(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment can only be initiated for pending orders"
            );
        }

        int amountInPaise = (int) Math.round(order.getTotalAmount() * 100);

        PaymentRequestDto request = new PaymentRequestDto();
        request.setOrderId(order.getId());
        request.setAmount(amountInPaise);

        CreatePaymentLinkResponse response= restTemplate.postForObject(
                paymentServiceUrl + "/payments/create-payment-link",
                request,
                CreatePaymentLinkResponse.class
        );
        if (response == null) {
           throw new RuntimeException("failed to create payment link");
        }
        order.setRazorpayPaymentLinkId(response.getId());
        orderRepository.save(order);

        return response;
    }
}
