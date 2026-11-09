package com.example.cartservice30oct26.service;

import com.example.cartservice30oct26.CartItemNotFoundException;
import com.example.cartservice30oct26.ProductNotFoundException;
import com.example.cartservice30oct26.dto.AddtoCartRequest;
import com.example.cartservice30oct26.dto.ProductResponseDto;
import com.example.cartservice30oct26.model.Cart;
import com.example.cartservice30oct26.model.CartItem;
import com.example.cartservice30oct26.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    public Cart getCartByUserId(Long userId){
        return cartRepository.findByuserId(userId)
                .orElseGet(() ->{
                    Cart cart=new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }
    public Cart addtoCart(AddtoCartRequest request){
        Cart cart=getCartByUserId(request.getUserId());
        ProductResponseDto product = getProduct(request.getProductId());
        CartItem existingItem=cart.getCartitems()
                .stream()
                .filter(item->item.getProductId()
                        .equals(request.getProductId()))
                                .findFirst()
                                .orElse(null);
          if(existingItem!=null){
              existingItem.setQuantity(
                existingItem.getQuantity()+request.getQuantity()
              );
              existingItem.setPrice(product.getPrice());
          }
          else{
        CartItem cartItem=new CartItem();
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(product.getPrice());
        cartItem.setCart(cart);

        cart.getCartitems().add(cartItem);
        }
        return cartRepository.save(cart);
    }
    public Cart updateCartItem(Long userId,Long productId,Integer Quantity){
        Cart cart=getCartByUserId(userId);
        CartItem existingItem=cart.getCartitems()
                .stream()
                .filter(item->item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(()->new CartItemNotFoundException("Product not found in the cart"+userId));
        existingItem.setQuantity(Quantity);
        return cartRepository.save(cart);
    }
    public Cart removeCartItem(Long userId, Long productId) {

        Cart cart = getCartByUserId(userId);

        CartItem existingItem = cart.getCartitems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Product not found in cart" + userId));

        cart.getCartitems().remove(existingItem);

        return cartRepository.save(cart);
    }
    public Cart clearCart(Long userId){
        Cart cart=getCartByUserId(userId);
        cart.getCartitems().clear();
        return cartRepository.save(cart);
    }
    public Double getCartTotal(Long userId){
        Cart cart=getCartByUserId(userId);
        return cart.getCartitems()
                .stream()
                .mapToDouble(item->item.getPrice()*item.getQuantity())
                .sum();
    }

    public CartService(CartRepository cartRepository,
                       RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }
    private ProductResponseDto getProduct(Long productId) {

        String url = "http://productservice/products/{id}";
       try {
           return restTemplate.getForObject(
                   url,
                   ProductResponseDto.class,
                   productId
           );
       }catch(HttpClientErrorException.NotFound e){
           throw new ProductNotFoundException(
                   "product not found with ID :" + productId
           );
       }catch(ResourceAccessException e){
           throw e;
       }
    }
}
