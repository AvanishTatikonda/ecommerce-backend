package com.example.cartservice30oct26.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private Long userId;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)

    private List<CartItem> cartitems=new ArrayList<>();

}
