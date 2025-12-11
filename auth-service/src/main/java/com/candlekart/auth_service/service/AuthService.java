package com.candlekart.auth_service.service;

import com.candlekart.auth_service.dto.UserRequest;
import com.candlekart.auth_service.dto.UserResponse;
import com.candlekart.auth_service.exc.UserAlreadyExistsException;
import com.candlekart.auth_service.dto.Token;
import com.candlekart.auth_service.dto.LoginRequest;
import com.candlekart.auth_service.exc.ValidationException;
import com.candlekart.auth_service.exc.WrongCredentialsException;
import com.candlekart.auth_service.feign.FeignUserClient;
import com.candlekart.auth_service.model.AuthDetails;
import com.candlekart.auth_service.repository.AuthRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FeignUserClient feignAccountClient;

    public Token login(LoginRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {

        //Password authentication happen here
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        if(authentication.isAuthenticated()){
            AuthDetails authDetails = authRepository.findByUsername(request.getUsername());
            return Token.builder()
                    .token(jwtService.generateToken(authDetails.getUsername(), authDetails.getUserId(), authDetails.getRole()))
                    .build();
        }else throw new BadCredentialsException("Bad credentials");
    }

    @Transactional
    public Token register(UserRequest request) throws Exception {

        // Step 1: Check if username already exists
        if (authRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username '" + request.getUsername() + "' is already taken.");
        }

        try{
            ResponseEntity<UserResponse> res = feignAccountClient.createUser(request);

            if(res.getStatusCode() == HttpStatus.CREATED){

                UserResponse response = res.getBody();

                assert response != null;
                AuthDetails authDetails = AuthDetails.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .userId(response.getUser_id())
                        .role(request.getRole()).build();

                authDetails = authRepository.save(authDetails);

                String token = jwtService.generateToken(authDetails.getUsername(), authDetails.getUserId(), authDetails.getRole());

                return Token.builder().token(token).build();
            }else{
                throw new WrongCredentialsException("Wrong Credentials");
            }
        }
        catch (FeignException.Conflict e) {
            throw new UserAlreadyExistsException(getErrorMessage(e));
        }
        catch (FeignException.BadRequest e) {
            throw new ValidationException(getErrorMessage(e));
        }
        catch (FeignException e) {
            throw new RuntimeException(getErrorMessage(e));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Map<String, Object>> validate(String authHeader) throws Exception {
        try{
            Map<String, Object> response = new HashMap<>();

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("valid", false);
                response.put("message", "Missing or invalid Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!jwtService.validateToken(authHeader)) {
                response.put("valid", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Claims claims = jwtService.extractAllClaims(authHeader);

            response.put("valid", true);
            response.put("userId", claims.get("userId"));
            response.put("username", claims.getSubject());
            response.put("role", claims.get("role"));

            return ResponseEntity.ok(response);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }










    private String getErrorMessage(FeignException e) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(e.contentUTF8());
        return json.get("error").asText();
    }
}
