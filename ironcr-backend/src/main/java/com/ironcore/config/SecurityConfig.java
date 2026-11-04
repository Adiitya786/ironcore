package com.ironcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SECURITY CONFIGURATION
 * ======================
 * Configures Spring Security for JWT-based authentication with role-based access.
 *
 * ACCESS RULES:
 * ============
 * PUBLIC (No authentication required):
 *   - POST /api/auth/**           → Login, Register
 *   - POST /api/trials            → Book a trial
 *   - POST /api/membership-requests → Join membership
 *   - POST /api/contact-messages  → Send contact message
 *   - GET  /api/programs/**       → View programs
 *   - GET  /api/equipment/**      → View equipment
 *   - GET  /api/trainers/**       → View trainers
 *   - GET  /api/gallery/**        → View gallery
 *   - GET  /api/health            → Health check
 *
 * ADMIN ONLY (Requires ROLE_ADMIN):
 *   - POST, PUT, DELETE /api/programs/**    → Manage programs
 *   - POST, PUT, DELETE /api/equipment/**   → Manage equipment
 *   - POST, PUT, DELETE /api/trainers/**    → Manage trainers
 *   - POST, PUT, DELETE /api/gallery/**     → Manage gallery
 *   - GET, PUT, DELETE /api/membership-requests/** → View/manage membership requests
 *   - GET, PUT, DELETE /api/contact-messages/**    → View/manage contact messages
 *
 * USER (Requires ROLE_USER or ROLE_ADMIN):
 *   - Can access their own profile data (future feature)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Main Security Filter Chain.
     * Defines which URLs require authentication and which roles can access them.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ===== STEP 1: Disable CSRF (not needed for stateless JWT) =====
            .csrf(csrf -> csrf.disable())

            // ===== STEP 2: Enable CORS =====
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ===== STEP 3: Set session to stateless (JWT doesn't use sessions) =====
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ===== STEP 4: Define URL-based authorization rules =====
            .authorizeHttpRequests(auth -> auth
                // ========== PUBLIC ENDPOINTS ==========
                // Authentication
                .requestMatchers("/api/auth/**").permitAll()

                // Health check
                .requestMatchers("/api/health").permitAll()

                // Public form submissions (no login required)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/trials").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/membership-requests").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/contact-messages").permitAll()

                // Public read-only access (anyone can view)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/programs/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/equipment/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/trainers/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/gallery/**").permitAll()

                // ========== ADMIN ONLY ENDPOINTS ==========
                // Program management (CREATE, UPDATE, DELETE)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/programs").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/programs/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/programs/**").hasRole("ADMIN")

                // Equipment management (CREATE, UPDATE, DELETE)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/equipment").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/equipment/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/equipment/**").hasRole("ADMIN")

                // Trainer management (CREATE, UPDATE, DELETE)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/trainers").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/trainers/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/trainers/**").hasRole("ADMIN")

                // Gallery management (CREATE, UPDATE, DELETE)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/gallery").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/gallery/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/gallery/**").hasRole("ADMIN")

                // Membership request management (VIEW, UPDATE STATUS, DELETE)
                .requestMatchers("/api/membership-requests/**").hasRole("ADMIN")

                // Contact message management (VIEW, UPDATE STATUS, DELETE)
                .requestMatchers("/api/contact-messages/**").hasRole("ADMIN")

                // ========== ALL OTHER REQUESTS ==========
                .anyRequest().authenticated()
            )

            // ===== STEP 5: Add JWT filter before UsernamePasswordAuthenticationFilter =====
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS Configuration.
     * Allows frontend (running on different port/domain) to call backend API.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow requests from frontend (adjust in production)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",     // Vite dev server
            "http://localhost:3000",     // React dev server
            "http://localhost:8081",     // Alternative port
            "http://127.0.0.1:5173",
            "http://127.0.0.1:3000"
        ));

        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers (including Authorization)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Register configuration for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Password Encoder Bean.
     * Uses BCrypt for secure password hashing.
     *
     * NOTE: Currently using NoOpPasswordEncoder for development (plain text).
     * In production, uncomment the BCryptPasswordEncoder return.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO: For production, use BCrypt
        // return new BCryptPasswordEncoder();

        // For development: plain text (no hashing)
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
}
