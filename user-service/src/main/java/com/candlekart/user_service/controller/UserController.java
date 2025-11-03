package com.candlekart.user_service.controller;

import com.candlekart.user_service.dto.UserRequest;
import com.candlekart.user_service.dto.UserResponse;
import com.candlekart.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<UserResponse> getUserByPhoneNumber(@PathVariable String phoneNumber){
        return ResponseEntity.ok(userService.getUserByPhoneNumber(phoneNumber));
    }
    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest request){
        return ResponseEntity.ok(userService.updateUser(request));
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
