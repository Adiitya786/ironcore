package com.ironcore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * User Entity for Authentication & Authorization.
 * Stores login credentials and role information.
 *
 * FIELDS:
 * - id: Primary key
 * - username: Unique username (email format recommended)
 * - email: Email address
 * - password: Hashed password (in production, use BCrypt)
 * - role: UserRole enum (ROLE_USER or ROLE_ADMIN)
 * - isActive: Account enabled/disabled flag
 * - createdAt: Registration timestamp
 * - updatedAt: Last modified timestamp
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Credentials =====

    @Column(nullable = false, unique = true, length = 100)
    private String username;      // Can be email or username

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;      // Should be hashed with BCrypt in production

    // ===== Role & Status =====

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;        // ROLE_USER or ROLE_ADMIN

    @Column(nullable = false)
    private Boolean isActive;     // Account enabled flag

    // ===== Timestamps =====

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ===== Lifecycle Hooks =====

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;  // Default: new users are active
        }
        if (role == null) {
            role = UserRole.ROLE_USER;  // Default: new users get USER role
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public User() {}

    // ===== Constructor for quick creation =====

    public User(String username, String email, String password, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = true;
    }

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
