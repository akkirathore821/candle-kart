package com.candlekart.connection.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/connection")
public class ConnectionController {
    @GetMapping()
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("This Connection Service : Connected");
    }
}
