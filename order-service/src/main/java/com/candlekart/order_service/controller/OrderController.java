package com.candlekart.order_service.controller;

import com.candlekart.order_service.dto.CartResponse;
import com.candlekart.order_service.dto.OrderResponse;
import com.candlekart.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CartResponse cart){
        return ResponseEntity.ok(orderService.createOrder(cart));
    }

    @GetMapping("/getOrdersByUser/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable String userId){
        return ResponseEntity.ok(orderService.getOrdersByUser(UUID.fromString(userId)));
    }

    @GetMapping("/getOrderById/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getOrderById(UUID.fromString(orderId)));
    }

    @DeleteMapping("/remove/{orderId}/{status}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @PathVariable String status){
        orderService.updateOrderStatus(UUID.fromString(orderId), status);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
