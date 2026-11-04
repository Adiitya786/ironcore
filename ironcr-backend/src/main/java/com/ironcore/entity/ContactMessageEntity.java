package com.ironcore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for Contact Us form submissions.
 * Stores messages from users on the Contact page.
 *
 * STATUS WORKFLOW:
 * - NEW → Just submitted
 * - READ → Admin has viewed
 * - RESPONDED → Reply sent to user
 * - CLOSED → Conversation complete
 */
@Entity
@Table(name = "contact_messages")
public class ContactMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Contact Information =====

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String message;

    // ===== Status Tracking =====

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

    // ===== Timestamps =====

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    /**
     * Message status lifecycle.
     */
    public enum MessageStatus {
        NEW,
        READ,
        RESPONDED,
        CLOSED
    }

    // ===== Pre-persist / pre-update hooks =====

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = MessageStatus.NEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
}
