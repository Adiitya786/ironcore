package com.ironcore.dto;

import com.ironcore.entity.MembershipRequestEntity;
import java.time.LocalDateTime;

/**
 * Response DTO for membership requests.
 * Returns saved membership request data to frontend.
 */
public class MembershipResponseDto {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String plan;
    private String fitnessGoal;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MembershipResponseDto() {}

    // ===== Getters =====

    public Long getId() { return id; }

    public String getFullName() { return fullName; }

    public String getPhone() { return phone; }

    public String getEmail() { return email; }

    public String getPlan() { return plan; }

    public String getFitnessGoal() { return fitnessGoal; }

    public String getStatus() { return status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ===== Setters =====

    public void setId(Long id) { this.id = id; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setEmail(String email) { this.email = email; }

    public void setPlan(String plan) { this.plan = plan; }

    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }

    public void setStatus(String status) { this.status = status; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ===== Factory Method =====

    /**
     * Convert MembershipRequestEntity to MembershipResponseDto
     */
    public static MembershipResponseDto fromEntity(MembershipRequestEntity entity) {
        MembershipResponseDto dto = new MembershipResponseDto();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setPlan(entity.getSelectedPlan().name());
        dto.setFitnessGoal(entity.getFitnessGoal());
        dto.setStatus(entity.getStatus().name());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}