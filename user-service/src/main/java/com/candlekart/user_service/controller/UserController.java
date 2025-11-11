package com.candlekart.user_service.controller;

import com.candlekart.user_service.dto.UserRequest;
import com.candlekart.user_service.dto.UserResponse;
import com.candlekart.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest request){
        return ResponseEntity.ok(userService.updateUser(request));
    }

//  Todo
//            | Method | Endpoint                | Description                          |
//            | ------ | ----------------------- | ------------------------------------ |
//            | POST   | `/users`                | Create user (called by Auth Service) |
//            | GET    | `/users/{id}`           | Fetch user profile                   |
//            | PUT    | `/users/{id}`           | Update profile                       |
//            | POST   | `/users/{id}/addresses` | Add address                          |
//            | GET    | `/users/{id}/addresses` | Get addresses                        |

}
