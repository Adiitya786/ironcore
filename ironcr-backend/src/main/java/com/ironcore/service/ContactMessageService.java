package com.ironcore.service;

import com.ironcore.dto.ContactRequestDto;
import com.ironcore.dto.ContactResponseDto;
import com.ironcore.entity.ContactMessageEntity;
import com.ironcore.repository.ContactMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for Contact Us message operations.
 * Handles saving, retrieving, and managing contact form submissions.
 *
 * TRANSACTION FLOW:
 * 1. Frontend submits form → POST /api/contact-messages
 * 2. Controller calls Service.saveContactMessage()
 * 3. Service converts DTO → Entity and saves to database
 * 4. On success: returns response with ID
 * 5. On error: throws exception → GlobalExceptionHandler returns 400/500
 *
 * All write operations are @Transactional (rollback on failure).
 */
@Service
public class ContactMessageService {

    private static final Logger log = LoggerFactory.getLogger(ContactMessageService.class);
    private final ContactMessageRepository repository;

    public ContactMessageService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Save a new contact message.
     *
     * TRANSACTION FLOW:
     * 1. Receive ContactRequestDto from controller
     * 2. Create new ContactMessageEntity
     * 3. Map all fields
     * 4. Set default status = NEW
     * 5. Save to database (all-or-nothing via @Transactional)
     * 6. Convert saved entity to response DTO
     * 7. Return response
     *
     * If ANY step fails, ENTIRE transaction rolls back.
     * No partial data is saved.
     */
    @Transactional
    public ContactResponseDto saveContactMessage(ContactRequestDto request) {
        log.info("Saving contact message from: {}", request.getName());

        // ===== STEP 1: Create new entity =====
        ContactMessageEntity entity = new ContactMessageEntity();

        // ===== STEP 2: Map all fields =====
        entity.setName(request.getName());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        entity.setSubject(request.getSubject());
        entity.setMessage(request.getMessage());

        // ===== STEP 3: Set default status =====
        // Status defaults to NEW automatically in @PrePersist
        // No manual setting needed - entity.onCreate() handles it

        // ===== STEP 4: Save to database (ALL OR NOTHING) =====
        // If this fails, ENTIRE transaction rolls back
        ContactMessageEntity saved = repository.save(entity);
        log.info("Contact message saved with id: {}", saved.getId());

        // ===== STEP 5: Convert to response and return =====
        return ContactResponseDto.fromEntity(saved);
    }

    /**
     * Get all contact messages.
     * Admin endpoint to view all submissions.
     */
    public List<ContactResponseDto> getAllMessages() {
        log.info("Fetching all contact messages");

        List<ContactMessageEntity> entities = repository.findAll();
        log.info("Found {} contact messages", entities.size());

        // Convert each entity to response DTO
        List<ContactResponseDto> responses = new ArrayList<>();
        for (ContactMessageEntity entity : entities) {
            responses.add(ContactResponseDto.fromEntity(entity));
        }
        return responses;
    }

    /**
     * Get contact message by ID.
     */
    public ContactResponseDto getMessageById(Long id) {
        log.info("Fetching contact message with id: {}", id);

        ContactMessageEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                log.error("Contact message not found with id: {}", id);
                return new RuntimeException("Contact message not found with id: " + id);
            });

        return ContactResponseDto.fromEntity(entity);
    }

    /**
     * Update message status.
     * Admin workflow: NEW → READ → RESPONDED → CLOSED
     *
     * TRANSACTION FLOW:
     * 1. Find entity by ID
     * 2. Update status field
     * 3. If status = RESPONDED, set respondedAt timestamp
     * 4. Save changes (all-or-nothing)
     */
    @Transactional
    public ContactResponseDto updateStatus(Long id, String newStatus) {
        log.info("Updating status for contact message {} to: {}", id, newStatus);

        // ===== STEP 1: Find the entity =====
        ContactMessageEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                log.error("Contact message not found with id: {}", id);
                return new RuntimeException("Contact message not found with id: " + id);
            });

        // ===== STEP 2: Validate and set status =====
        try {
            ContactMessageEntity.MessageStatus status =
                ContactMessageEntity.MessageStatus.valueOf(newStatus.toUpperCase());
            entity.setStatus(status);

            // ===== STEP 3: Set respondedAt if marked as RESPONDED =====
            if (status == ContactMessageEntity.MessageStatus.RESPONDED) {
                entity.setRespondedAt(LocalDateTime.now());
                log.debug("Marked message {} as responded at: {}", id, entity.getRespondedAt());
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid status: {}. Must be: NEW, READ, RESPONDED, CLOSED", newStatus);
            throw new RuntimeException("Invalid status: " + newStatus);
        }

        // ===== STEP 4: Save changes (all-or-nothing) =====
        ContactMessageEntity saved = repository.save(entity);
        log.info("Status updated for contact message {}", id);

        // ===== STEP 5: Return updated response =====
        return ContactResponseDto.fromEntity(saved);
    }

    /**
     * Delete a contact message.
     * Admin use only - for removing spam or archived messages.
     */
    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting contact message with id: {}", id);

        // ===== STEP 1: Check if exists =====
        if (!repository.existsById(id)) {
            log.error("Contact message not found with id: {}", id);
            throw new RuntimeException("Contact message not found with id: " + id);
        }

        // ===== STEP 2: Delete (all-or-nothing) =====
        repository.deleteById(id);
        log.info("Contact message {} deleted successfully", id);
    }
}