package com.ironcore.dto;

/**
 * Data Transfer Object for Trainer response.
 * Used to send trainer data back to client.
 */
public class TrainerResponse {

    // Trainer identifier
    private Long id;

    // Trainer name
    private String name;

    // Trainer bio
    private String bio;

    // Image filename
    private String imageName;

    // Image type (MIME type)
    private String imageType;

    // Image data (bytes) - will be base64 encoded in JSON
    private byte[] imageData;

    // Timestamps
    private String createdAt;

    // Default constructor
    public TrainerResponse() {}

    // ===== Getter and Setter Methods =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}