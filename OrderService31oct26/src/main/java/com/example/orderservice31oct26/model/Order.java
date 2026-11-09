package com.example.orderservice31oct26.model;

import com.example.orderservice31oct26.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Long userId;
    private Double totalAmount;
    private String razorpayOrderId;
//    private String status;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Orderitem> orderitems=new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String razorpayPaymentLinkId;
}
