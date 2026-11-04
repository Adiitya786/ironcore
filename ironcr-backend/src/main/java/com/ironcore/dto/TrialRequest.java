package com.ironcore.dto;

import jakarta.validation.constraints.*;

/**
 * Request DTO for booking a trial session.
 * All validation rules are here so errors are clear and specific.
 */
public class TrialRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name too long")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be exactly 10 digits")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Preferred date is required")
    private String preferredDate;

    @NotBlank(message = "Preferred time is required")
    private String preferredTime;

    private String fitnessGoal;

    private String experience;

    private String message;

    // Getters
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPreferredDate() { return preferredDate; }
    public String getPreferredTime() { return preferredTime; }
    public String getFitnessGoal() { return fitnessGoal; }
    public String getExperience() { return experience; }
    public String getMessage() { return message; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPreferredDate(String preferredDate) { this.preferredDate = preferredDate; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }
    public void setExperience(String experience) { this.experience = experience; }
    public void setMessage(String message) { this.message = message; }
}