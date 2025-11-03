package com.candlekart.auth_service.feign;


import com.candlekart.auth_service.dto.UserRequest;
import com.candlekart.auth_service.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/api/user")
public interface FeignAccountClient {

    @PostMapping(value = "/create", consumes = "application/json")
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request);

}