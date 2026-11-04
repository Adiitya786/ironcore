package com.ironcore.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Request DTO for membership join form.
 * Matches the frontend form fields in Join.jsx
 */
public class MembershipRequestDto {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name too long")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be exactly 10 digits")
    private String phone;

    @Email(message = "Please enter a valid email address")
    private String email;

    @NotNull(message = "Plan selection is required")
    private String plan; // "basic", "standard", "premium" - matches frontend IDs

    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate preferredStartDate;

    @Size(max = 50, message = "Fitness goal too long")
    private String fitnessGoal;

    // ===== Getters =====

    public String getFullName() { return fullName; }

    public String getPhone() { return phone; }

    public String getEmail() { return email; }

    public String getPlan() { return plan; }

    public LocalDate getPreferredStartDate() { return preferredStartDate; }

    public String getFitnessGoal() { return fitnessGoal; }

    // ===== Setters =====

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setEmail(String email) { this.email = email; }

    public void setPlan(String plan) { this.plan = plan; }

    public void setPreferredStartDate(LocalDate preferredStartDate) {
        this.preferredStartDate = preferredStartDate;
    }

    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }
}