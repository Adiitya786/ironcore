package com.ironcore.service;

import com.ironcore.dto.MembershipRequestDto;
import com.ironcore.dto.MembershipResponseDto;
import com.ironcore.entity.MembershipPlan;
import com.ironcore.entity.MembershipRequestEntity;
import com.ironcore.repository.MembershipRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for Membership Request operations.
 * Handles saving, retrieving, and managing membership requests.
 *
 * FLOW EXPLANATION:
 * 1. Frontend submits form → POST /api/membership-requests
 * 2. Controller calls Service.saveMembershipRequest()
 * 3. Service converts DTO → Entity and saves to database
 * 4. On success: returns response with ID for frontend confirmation
 * 5. On error: throws exception → GlobalExceptionHandler returns 400/500
 */
@Service
public class MembershipRequestService {

    private static final Logger logger = LoggerFactory.getLogger(MembershipRequestService.class);

    private final MembershipRequestRepository repository;

    public MembershipRequestService(MembershipRequestRepository repository) {
        this.repository = repository;
        logger.info("MembershipRequestService initialized");
    }

    /**
     * Save a new membership request.
     *
     * TRANSACTION FLOW:
     * 1. Receive MembershipRequestDto from controller
     * 2. Create new MembershipRequestEntity
     * 3. Map all fields (with plan conversion: "basic" → BASIC enum)
     * 4. Save to database (all-or-nothing via @Transactional)
     * 5. Convert saved entity to response DTO
     * 6. Return response
     *
     * If ANY step fails, ENTIRE transaction rolls back.
     * No partial data is saved to database.
     *
     * @param request - Frontend form data
     * @return MembershipResponseDto - Saved request with ID and timestamps
     * @throws RuntimeException - If validation fails or database error
     */
    @Transactional
    public MembershipResponseDto saveMembershipRequest(MembershipRequestDto request) {
        logger.info("Saving membership request for: {}", request.getFullName());

        // ===== STEP 1: Create new entity =====
        MembershipRequestEntity entity = new MembershipRequestEntity();

        // ===== STEP 2: Map personal info =====
        entity.setFullName(request.getFullName());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        entity.setFitnessGoal(request.getFitnessGoal());
        entity.setPreferredStartDate(request.getPreferredStartDate());

        // ===== STEP 3: Convert plan string to enum =====
        // Frontend sends: "basic", "standard", "premium"
        // Backend expects: MembershipPlan.BASIC, STANDARD, PREMIUM
        String planString = request.getPlan().toUpperCase();
        try {
            MembershipPlan plan = MembershipPlan.valueOf(planString);
            entity.setSelectedPlan(plan);
            logger.debug("Mapped plan: {} → {}", request.getPlan(), plan);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid plan: {}. Must be: basic, standard, premium", request.getPlan());
            throw new RuntimeException("Invalid membership plan: " + request.getPlan());
        }

        // ===== STEP 4: Set default status =====
        // Status defaults to NEW automatically in @PrePersist
        // No manual setting needed - entity.onCreate() handles it

        // ===== STEP 5: Save to database (ALL OR NOTHING) =====
        // If this fails, ENTIRE transaction rolls back
        // No partial data is saved
        MembershipRequestEntity saved = repository.save(entity);
        logger.info("Membership request saved with id: {}", saved.getId());

        // ===== STEP 6: Convert to response and return =====
        return MembershipResponseDto.fromEntity(saved);
    }

    /**
     * Get all membership requests.
     * Admin endpoint to view all requests.
     *
     * @return List of all membership requests ordered by created_at desc
     */
    public List<MembershipResponseDto> getAllMembershipRequests() {
        logger.info("Fetching all membership requests");

        List<MembershipRequestEntity> entities = repository.findAll();
        logger.info("Found {} membership requests", entities.size());

        // Convert each entity to response DTO
        List<MembershipResponseDto> responses = new ArrayList<>();
        for (MembershipRequestEntity entity : entities) {
            responses.add(MembershipResponseDto.fromEntity(entity));
        }

        return responses;
    }

    /**
     * Get membership request by ID.
     *
     * @param id - Request ID
     * @return MembershipResponseDto
     * @throws RuntimeException - If not found
     */
    public MembershipResponseDto getMembershipRequestById(Long id) {
        logger.info("Fetching membership request with id: {}", id);

        MembershipRequestEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Membership request not found with id: {}", id);
                return new RuntimeException("Membership request not found with id: " + id);
            });

        return MembershipResponseDto.fromEntity(entity);
    }

    /**
     * Update membership request status.
     * Admin use: CONTACTED → CONFIRMED or REJECTED
     *
     * TRANSACTION FLOW:
     * 1. Find entity by ID
     * 2. Update status field
     * 3. Save changes
     * 4. If any step fails, roll back entire transaction
     *
     * @param id - Request ID
     * @param newStatus - NEW, CONTACTED, CONFIRMED, or REJECTED
     * @return Updated response DTO
     */
    @Transactional
    public MembershipResponseDto updateStatus(Long id, String newStatus) {
        logger.info("Updating status for membership request {} to: {}", id, newStatus);

        // ===== STEP 1: Find the entity =====
        MembershipRequestEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Membership request not found with id: {}", id);
                return new RuntimeException("Membership request not found with id: " + id);
            });

        // ===== STEP 2: Validate status value =====
        try {
            MembershipRequestEntity.RequestStatus status =
                MembershipRequestEntity.RequestStatus.valueOf(newStatus.toUpperCase());
            entity.setStatus(status);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status: {}. Must be: NEW, CONTACTED, CONFIRMED, REJECTED", newStatus);
            throw new RuntimeException("Invalid status: " + newStatus);
        }

        // ===== STEP 3: Save changes (all-or-nothing) =====
        MembershipRequestEntity saved = repository.save(entity);
        logger.info("Status updated for membership request {}", id);

        // ===== STEP 4: Return updated response =====
        return MembershipResponseDto.fromEntity(saved);
    }

    /**
     * Delete a membership request.
     * Admin use only - for removing spam or duplicate requests.
     *
     * TRANSACTION FLOW:
     * 1. Check if entity exists
     * 2. Delete from database
     * 3. If any step fails, roll back
     *
     * @param id - Request ID to delete
     */
    @Transactional
    public void deleteMembershipRequest(Long id) {
        logger.info("Deleting membership request with id: {}", id);

        // ===== STEP 1: Check if exists =====
        if (!repository.existsById(id)) {
            logger.error("Membership request not found with id: {}", id);
            throw new RuntimeException("Membership request not found with id: " + id);
        }

        // ===== STEP 2: Delete (all-or-nothing) =====
        repository.deleteById(id);
        logger.info("Membership request {} deleted successfully", id);
    }
}