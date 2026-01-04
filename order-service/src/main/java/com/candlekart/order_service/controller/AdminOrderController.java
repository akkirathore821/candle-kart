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
@RequestMapping("api/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CartResponse cart){
        return ResponseEntity.ok(orderService.createOrder(cart));
    }
    @PutMapping("/updateStatus/{orderId}/{status}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @PathVariable String status){
        orderService.updateOrderStatus(UUID.fromString(orderId), status);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
