//package com.candlekart.user_service.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.*;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.candlekart.user_service.constants.Constants.PUBLIC_SECRET;
//
//@Slf4j
//@Component
//public class JwtUtils {
//
//    public PublicKey getPublicSignKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
//        byte[] decoded = Base64.getDecoder().decode(PUBLIC_SECRET);
//        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
//    }
//
//    public Claims extractAllClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return Jwts.parserBuilder()
//                .setSigningKey(getPublicSignKey())
//                .build()
//                .parseClaimsJws(token.replace("Bearer ", ""))
//                .getBody();
//    }
//    public String extractUsername(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return extractAllClaims(token).getSubject();
//    }
//    public String extractUserId(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return extractAllClaims(token).get("userId",String.class);
//    }
//    public String extractRoles(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return extractAllClaims(token).get("role",String.class);
//    }
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getPublicSignKey())
//                    .build()
//                    .parseClaimsJws(token.replace("Bearer ", ""));
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        Date expiration = extractAllClaims(token).getExpiration();
//        return expiration.before(new Date());
//    }
//}
