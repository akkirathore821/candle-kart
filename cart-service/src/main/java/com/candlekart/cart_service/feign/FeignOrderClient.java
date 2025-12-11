package com.candlekart.cart_service.feign;

import com.candlekart.cart_service.dto.CartResponse;
import com.candlekart.cart_service.dto.OrderResponse;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", path = "/api/order")
public interface FeignOrderClient {

    @PostMapping(value = "/create", consumes = "application/json")
    ResponseEntity<OrderResponse> createOrder(@RequestBody CartResponse cart);
}
