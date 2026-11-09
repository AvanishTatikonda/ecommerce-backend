package com.example.cartservice30oct26.controller;

import com.example.cartservice30oct26.dto.AddtoCartRequest;
import com.example.cartservice30oct26.dto.UpdateCartItemRequest;
import com.example.cartservice30oct26.model.Cart;
import com.example.cartservice30oct26.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public Cart addToCart(@Valid @RequestBody AddtoCartRequest request) {
        return cartService.addtoCart(request);
    }

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PutMapping("/{userId}/update/{productId}")
    public Cart updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long productId,
           @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateCartItem(
                userId,
                productId,
                request.getQuantity()
        );
    }
        @DeleteMapping("/{userId}/remove/{productId}")
        public  Cart removeCartItem(
                @PathVariable Long userId,
                @PathVariable Long productId)
                {
            return cartService.removeCartItem(userId,productId);
        }
    @DeleteMapping("/{userId}/clear")
    public Cart clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }
    @GetMapping("/{userId}/total")
    public  Double getCartTotal(@PathVariable Long userId){
        return cartService.getCartTotal(userId);
    }
}