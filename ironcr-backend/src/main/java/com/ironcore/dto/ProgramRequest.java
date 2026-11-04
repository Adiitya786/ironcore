package com.ironcore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating/updating a Program.
 * Used to receive data from client requests.
 */
public class ProgramRequest {

    // Title is required and has maximum length limit
    private String title;

    // Description can be empty or null
    private String description;

    // Image filename - name of the uploaded image
    private String imageName;

    // Image type - MIME type (e.g., "image/jpeg", "image/png")
    private String imageType;

    // Image bytes - actual image data
    private byte[] imageData;

    // Benefits stored as comma-separated values or JSON
    private String benefits;

    // ===== Getter and Setter Methods =====

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
}