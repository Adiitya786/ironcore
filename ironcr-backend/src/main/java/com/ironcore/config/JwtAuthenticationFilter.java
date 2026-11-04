package com.ironcore.config;

import com.ironcore.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT Authentication Filter
 * ==========================
 * Intercepts every HTTP request and validates the JWT token.
 * If valid, sets the user in Spring Security context.
 *
 * HOW IT WORKS:
 * ==============
 * 1. Client sends request with header: Authorization: Bearer <token>
 * 2. Filter extracts token from header
 * 3. Validates token using JwtUtil
 * 4. If valid → creates Authentication object with username + role
 * 5. Sets authentication in SecurityContextHolder (makes user available to controllers)
 * 6. Request continues to controller
 *
 * If token is missing/invalid:
 * - Request continues (not blocked at filter level)
 * - Spring Security's authorization rules (in SecurityConfig) will block
 * - Admin endpoints will return 403 Forbidden for unauthenticated requests
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // ===== STEP 1: Extract Authorization header =====
        String authorizationHeader = request.getHeader("Authorization");

        // ===== STEP 2: Check if header exists and starts with "Bearer " =====
        String token = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            try {
                username = jwtUtil.extractUsername(token);  // Extract username from token
            } catch (Exception e) {
                log.debug("Failed to extract username from token: {}", e.getMessage());
            }
        }

        // ===== STEP 3: If token is valid and username extracted =====
        if (token != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtUtil.validateToken(token, username)) {
                // ===== STEP 4: Get role from token (already includes ROLE_ prefix) =====
                String role = jwtUtil.extractRole(token);  // Returns "ROLE_ADMIN" or "ROLE_USER"

                // ===== STEP 5: Create authentication token with authorities =====
                // Note: role already has "ROLE_" prefix, don't add it again
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));

                authenticationToken.setDetails(null);

                // ===== STEP 6: Set authentication in Spring Security context =====
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                log.debug("JWT Authentication successful for user: {} (role: {})", username, role);
            }
        }

        // ===== STEP 7: Continue with request (filter chain) =====
        filterChain.doFilter(request, response);
    }
}
