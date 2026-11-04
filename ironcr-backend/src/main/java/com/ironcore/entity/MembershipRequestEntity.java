package com.ironcore.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a membership join request.
 * Stores user details when someone submits the "Join Now" form.
 *
 * This is an INQUIRY/REQUEST - not a completed membership.
 * Admin will review and contact the user to complete the membership.
 */
@Entity
@Table(name = "membership_requests")
public class MembershipRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Personal Information =====

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 100)
    private String email;

    // ===== Membership Details =====

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipPlan selectedPlan;

    @Column(name = "preferred_start_date")
    private LocalDate preferredStartDate;

    @Column(name = "fitness_goal", length = 50)
    private String fitnessGoal;

    // ===== Request Status =====

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    // ===== Timestamps =====

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Status of the membership request.
     * NEW = Just submitted
     * CONTACTED = Admin has reached out
     * CONFIRMED = User has completed membership
     * REJECTED = Request declined
     */
    public enum RequestStatus {
        NEW,
        CONTACTED,
        CONFIRMED,
        REJECTED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Default status for new requests
        if (status == null) {
            status = RequestStatus.NEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public MembershipRequestEntity() {}

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public MembershipPlan getSelectedPlan() { return selectedPlan; }
    public void setSelectedPlan(MembershipPlan selectedPlan) { this.selectedPlan = selectedPlan; }

    public LocalDate getPreferredStartDate() { return preferredStartDate; }
    public void setPreferredStartDate(LocalDate preferredStartDate) { this.preferredStartDate = preferredStartDate; }

    public String getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}