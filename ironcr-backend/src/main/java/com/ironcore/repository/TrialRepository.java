package com.ironcore.repository;

import com.ironcore.entity.Trial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Trial bookings.
 * Provides database access methods.
 */
@Repository
public interface TrialRepository extends JpaRepository<Trial, Long> {
}