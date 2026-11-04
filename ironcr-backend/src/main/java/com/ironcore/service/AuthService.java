package com.ironcore.service;

import com.ironcore.dto.AuthResponse;
import com.ironcore.dto.LoginRequest;
import com.ironcore.dto.RegisterRequest;
import com.ironcore.entity.User;
import com.ironcore.entity.UserRole;
import com.ironcore.repository.UserRepository;
import com.ironcore.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service.
 * Handles user registration, login, and JWT token generation.
 *
 * AUTHENTICATION FLOW:
 * ==================
 * REGISTRATION:
 * 1. User submits registration form → POST /api/auth/register
 * 2. Check if username/email already exists
 * 3. Create new User entity with ROLE_USER (default)
 * 4. Save to database (password stored as plain text for now — BCrypt in production)
 * 5. Generate JWT token with username + role
 * 6. Return AuthResponse with token
 *
 * LOGIN:
 * 1. User submits login form → POST /api/auth/login
 * 2. Find user by username
 * 3. Validate password (plain text comparison for now — BCrypt in production)
 * 4. Generate JWT token with username + role
 * 5. Return AuthResponse with token
 *
 * NOTE: In production, use Spring Security's BCryptPasswordEncoder
 * to hash passwords before storing and for validation.
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // ===================================================================
    //  REGISTRATION
    // ===================================================================

    /**
     * Register a new user.
     *
     * STEPS:
     * 1. Check if username already exists
     * 2. Check if email already exists
     * 3. Create new User entity with ROLE_USER
     * 4. Save to database
     * 5. Generate JWT token
     * 6. Return response with token
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        // ===== STEP 1: Check if username exists =====
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Username already exists: {}", request.getUsername());
            return new AuthResponse("Username already exists");
        }

        // ===== STEP 2: Check if email exists =====
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            return new AuthResponse("Email already exists");
        }

        // ===== STEP 3: Create new user (default role: ROLE_USER) =====
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());  // TODO: Hash with BCrypt in production
        user.setRole(UserRole.ROLE_USER);         // Default role for new registrations
        user.setIsActive(true);

        // ===== STEP 4: Save to database =====
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        // ===== STEP 5: Generate JWT token =====
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name());

        // ===== STEP 6: Return response =====
        return new AuthResponse(
            token,
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getRole().name(),
            "Registration successful"
        );
    }

    // ===================================================================
    //  LOGIN
    // ===================================================================

    /**
     * Authenticate user and generate JWT token.
     *
     * STEPS:
     * 1. Find user by username
     * 2. Validate password
     * 3. Check if account is active
     * 4. Generate JWT token
     * 5. Return response with token
     */
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        // ===== STEP 1: Find user by username =====
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                log.warn("User not found: {}", request.getUsername());
                return new RuntimeException("Invalid username or password");
            });

        // ===== STEP 2: Validate password =====
        // TODO: In production, use BCryptPasswordEncoder.matches(rawPassword, encodedPassword)
        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("Invalid password for user: {}", request.getUsername());
            throw new RuntimeException("Invalid username or password");
        }

        // ===== STEP 3: Check if account is active =====
        if (!user.getIsActive()) {
            log.warn("Inactive account attempted login: {}", request.getUsername());
            throw new RuntimeException("Account is disabled");
        }

        // ===== STEP 4: Generate JWT token =====
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        log.info("Login successful for user: {} (role: {})", user.getUsername(), user.getRole());

        // ===== STEP 5: Return response =====
        return new AuthResponse(
            token,
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            "Login successful"
        );
    }
}
