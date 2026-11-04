package com.ironcore.controller;

import com.ironcore.dto.AuthResponse;
import com.ironcore.dto.LoginRequest;
import com.ironcore.dto.RegisterRequest;
import com.ironcore.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AUTHENTICATION CONTROLLER
 * =========================
 * Public endpoints for user registration and login.
 *
 * ENDPOINTS:
 * POST /api/auth/register → Register new user (ROLE_USER by default)
 * POST /api/auth/login    → Authenticate user, return JWT token
 *
 * Both endpoints are PUBLIC (no authentication required).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * REGISTER NEW USER
     * =================
     * Creates a new user account with ROLE_USER by default.
     * Returns JWT token on success.
     *
     * Request Body:
     * {
     *   "username": "user@example.com",
     *   "email": "user@example.com",
     *   "password": "password123"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/auth/register | username={}", request.getUsername());

        AuthResponse response = authService.register(request);

        if (response.getToken() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * LOGIN USER
     * ==========
     * Authenticates user credentials and returns JWT token.
     *
     * Request Body:
     * {
     *   "username": "admin@ironcore.com",
     *   "password": "Admin@123"
     * }
     *
     * Response:
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "username": "admin@ironcore.com",
     *   "email": "admin@ironcore.com",
     *   "role": "ROLE_ADMIN",
     *   "message": "Login successful"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login | username={}", request.getUsername());

        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(e.getMessage()));
        }
    }
}
