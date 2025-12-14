package com.candlekart.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connection/auth")
public class ConnectionCheckController {
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Auth Service");
    }
}