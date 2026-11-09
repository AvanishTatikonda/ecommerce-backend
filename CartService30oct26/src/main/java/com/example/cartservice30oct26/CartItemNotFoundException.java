package com.example.cartservice30oct26;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message){
        super(message);
    }
}
