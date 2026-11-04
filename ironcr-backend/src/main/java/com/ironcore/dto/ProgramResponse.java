package com.ironcore.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Program response.
 * Used to send program data back to client.
 */
public class ProgramResponse {

    // Program identifier
    private Long id;

    // Program title
    private String title;

    // Program description
    private String description;

    // Image filename
    private String imageName;

    // Image type (MIME type)
    private String imageType;

    // Image data (bytes) - will be base64 encoded in JSON
    private byte[] imageData;

    // Program benefits
    private String benefits;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public ProgramResponse() {}

    // ===== Getter and Setter Methods =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}