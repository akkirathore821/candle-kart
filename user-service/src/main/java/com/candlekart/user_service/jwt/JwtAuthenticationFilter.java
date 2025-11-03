//package com.candlekart.user_service.jwt;
//
//import com.candlekart.user_service.exc.InvalidTokenException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Slf4j
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        // 1️⃣ Extract token from Authorization header
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7); // remove "Bearer "
//        if(jwtUtils.validateToken(jwt)){
//            try {
//                String username = jwtUtils.extractUsername(jwt);
//                String role = jwtUtils.extractRoles(jwt);
//                // Create Spring Security authentication
//                var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
//                Authentication authentication =
//                        new UsernamePasswordAuthenticationToken(username, null, authorities);
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                filterChain.doFilter(request, response);
//            } catch (Exception e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            }
//
//        }else response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//}
