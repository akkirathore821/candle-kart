package com.candlekart.connection.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/connection")
@Slf4j
public class PublicConnectionController {
    @GetMapping()
    public ResponseEntity<String> check(HttpServletRequest request) {
        return ResponseEntity.ok("This Connection Service : Connected : " + request.getRemoteAddr());
    }
}
