package com.ironcore.repository;

import com.ironcore.entity.MembershipRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for MembershipRequestEntity operations.
 * Provides CRUD operations for membership requests.
 */
@Repository
public interface MembershipRequestRepository extends JpaRepository<MembershipRequestEntity, Long> {

    /**
     * Find all requests with a specific status.
     * Useful for admin to filter: NEW, CONTACTED, CONFIRMED, REJECTED
     */
    List<MembershipRequestEntity> findByStatus(MembershipRequestEntity.RequestStatus status);

    /**
     * Find requests by phone number.
     * Useful to check for duplicates or existing requests.
     */
    List<MembershipRequestEntity> findByPhone(String phone);

    /**
     * Find requests by email.
     * Useful to check for duplicates or existing requests.
     */
    List<MembershipRequestEntity> findByEmail(String email);

    /**
     * Check if a request exists with the given phone number and status.
     */
    boolean existsByPhoneAndStatus(String phone, MembershipRequestEntity.RequestStatus status);
}