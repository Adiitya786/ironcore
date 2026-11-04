package com.ironcore.dto;

/**
 * Response DTO for authentication endpoints.
 * Returned after successful login or registration.
 *
 * FRONTEND USAGE:
 * - Store token in localStorage: localStorage.setItem('token', response.token)
 * - Store role in localStorage: localStorage.setItem('role', response.role)
 * - Add to all API requests: headers: { 'Authorization': `Bearer ${token}` }
 */
public class AuthResponse {

    private String token;        // JWT token
    private String username;     // User's username
    private String email;        // User's email
    private String role;         // "ROLE_USER" or "ROLE_ADMIN"
    private String message;      // Success/error message

    // ===== Constructor for successful auth =====
    public AuthResponse(String token, String username, String email, String role, String message) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    // ===== Constructor for error response (no token) =====
    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse() {}

    // ===== Getters and Setters =====

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
