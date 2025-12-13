package com.candlekart.cart_service.controller;

import com.candlekart.cart_service.dto.CartItemRequest;
import com.candlekart.cart_service.dto.CartResponse;
import com.candlekart.cart_service.dto.OrderResponse;
import com.candlekart.cart_service.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/addItem/{userId}")
    public ResponseEntity<CartResponse> addItem(@PathVariable String userId, @RequestBody CartItemRequest request){
        return ResponseEntity.ok(cartService.addItem(userId, request));
    }

    @DeleteMapping("/remove/{userId}/{sku}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable String userId, @PathVariable String sku){
        return ResponseEntity.ok(cartService.removeItem(userId, sku));
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrderResponse> checkout(@PathVariable String userId) throws JsonProcessingException {
        return ResponseEntity.ok(cartService.checkout(userId));
    }


}
