package com.ironcore.repository;

import com.ironcore.entity.ContactMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ContactMessageEntity CRUD operations.
 * Provides queries for filtering by status, date, phone, etc.
 */
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessageEntity, Long> {

    /**
     * Find all messages by status.
     * Admin use: NEW → unread, RESPONDED → handled.
     */
    List<ContactMessageEntity> findByStatus(ContactMessageEntity.MessageStatus status);

    /**
     * Find messages by phone number.
     * Useful for checking previous inquiries from same user.
     */
    List<ContactMessageEntity> findByPhone(String phone);

    /**
     * Find messages by email.
     */
    List<ContactMessageEntity> findByEmail(String email);

    /**
     * Find messages with email present (not null/empty).
     */
    List<ContactMessageEntity> findByEmailIsNotNull();

    /**
     * Find messages created after a given date.
     * For reports: "messages from last week"
     */
    List<ContactMessageEntity> findByCreatedAtAfter(java.time.LocalDateTime after);

    /**
     * Find messages by subject containing keyword (case-insensitive).
     */
    List<ContactMessageEntity> findBySubjectContainingIgnoreCase(String keyword);
}
