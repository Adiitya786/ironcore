package com.ironcore.repository;

import com.ironcore.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Trainer entity.
 * Provides database operations for trainer data.
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    // JpaRepository provides all basic CRUD operations:
    // - save(Trainer)
    // - findById(Long)
    // - findAll()
    // - delete(Trainer)
    // - deleteById(Long)
}