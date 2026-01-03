package com.candlekart.auth_service.controller;

import com.candlekart.auth_service.dto.Token;
import com.candlekart.auth_service.dto.LoginRequest;
import com.candlekart.auth_service.dto.UserRequest;
import com.candlekart.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@RestController
@RequestMapping("/api/user/auth")
public class UserAuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestHeader("Authorization") String authHeader) throws Exception {
        return authService.validate(authHeader);
    }
}

// Check Points