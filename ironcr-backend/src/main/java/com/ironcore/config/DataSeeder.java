package com.ironcore.config;

import com.ironcore.entity.User;
import com.ironcore.entity.UserRole;
import com.ironcore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DEFAULT ADMIN SEED
 * ===================
 * Automatically creates a default administrator account when the application starts.
 * This ensures there's always an admin available to manage the gym.
 *
 * DEFAULT CREDENTIALS (for first-time access):
 * ======
 * Username: admin@ironcore.com
 * Password: Admin@123
 * Role: ROLE_ADMIN
 *
 * SECURITY NOTE:
 * - These credentials should be changed immediately after first login
 * - In production, set credentials via environment variables
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // ===== STEP 1: Check if admin already exists =====
        if (userRepository.existsByUsername("admin@ironcore.com")) {
            log.info("Admin account already exists — skipping seed");
            return;
        }

        // ===== STEP 2: Create default admin account =====
        User admin = new User();
        admin.setUsername("admin@ironcore.com");
        admin.setEmail("admin@ironcore.com");
        admin.setPassword("Admin@123");  // TODO: Hash with BCrypt in production
        admin.setRole(UserRole.ROLE_ADMIN);
        admin.setIsActive(true);

        // ===== STEP 3: Save to database =====
        User savedAdmin = userRepository.save(admin);
        log.info("=== DEFAULT ADMIN CREATED ===");
        log.info("Username: {}", savedAdmin.getUsername());
        log.info("Email: {}", savedAdmin.getEmail());
        log.info("Role: {}", savedAdmin.getRole());
        log.info("Password: Admin@123 (CHANGE AFTER FIRST LOGIN)");
        log.info("==============================");
    }
}
