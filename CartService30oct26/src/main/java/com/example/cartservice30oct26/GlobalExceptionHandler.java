package com.example.cartservice30oct26;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(
            ProductNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    @ExceptionHandler(org.springframework.web.client.ResourceAccessException.class)
    public ResponseEntity<String> handleProductServiceUnavailable(
            org.springframework.web.client.ResourceAccessException ex){
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("ProductService is currently unavailable.please try again later.");
    }
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFound(
            CartItemNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
