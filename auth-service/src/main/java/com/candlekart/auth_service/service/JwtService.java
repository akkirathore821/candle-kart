package com.candlekart.auth_service.service;

import com.candlekart.auth_service.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import static com.candlekart.auth_service.constants.Constants.PRIVATE_SECRET;
import static com.candlekart.auth_service.constants.Constants.PUBLIC_SECRET;

@Service
public class JwtService {

    private final UserDetailsServiceImpl userDetailsService;

    public JwtService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(String username, UUID userId, Role role) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",userId);
        claims.put("role", role);
        return createToken(claims, userDetails);
    }
    public String createToken(Map<String, Object> claims, UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
//                .setIssuer(userDetails.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getPrivateSignKey(), SignatureAlgorithm.RS256).compact();
    }

    public PrivateKey getPrivateSignKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decoded = Base64.getDecoder().decode(PRIVATE_SECRET);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }
    public PublicKey getPublicSignKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decoded = Base64.getDecoder().decode(PUBLIC_SECRET);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }

    public Claims extractAllClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicSignKey())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    public String extractUsername(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractAllClaims(token).getSubject();
    }
    public String extractUserId(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractAllClaims(token).get("userId",String.class);
    }
    public String extractRoles(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractAllClaims(token).get("role",String.class);
    }
    public boolean isTokenValid(String token, UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean validateToken(String token) {
        try {
            return extractAllClaims(token).getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }
}
