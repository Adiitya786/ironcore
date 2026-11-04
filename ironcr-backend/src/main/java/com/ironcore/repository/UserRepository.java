package com.ironcore.repository;

import com.ironcore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User CRUD operations.
 * Provides database queries for authentication and user management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username.
     * Used during login to validate credentials.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email.
     * Used to check if email already registered or for password reset.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a username already exists.
     * Used during registration to prevent duplicates.
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email already exists.
     * Used during registration to prevent duplicate emails.
     */
    boolean existsByEmail(String email);
}
