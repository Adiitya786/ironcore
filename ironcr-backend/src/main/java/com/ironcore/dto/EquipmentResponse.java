package com.ironcore.dto;

import com.ironcore.entity.EquipmentCategory;

/**
 * Data Transfer Object for Equipment response.
 * Used to send equipment data back to client.
 */
public class EquipmentResponse {

    // Equipment identifier
    private Long id;

    // Equipment name
    private String name;

    // Equipment description
    private String description;

    // Equipment category
    private EquipmentCategory category;

    // Image filename
    private String imageName;

    // Image type (MIME type)
    private String imageType;

    // Image data (bytes) - will be base64 encoded in JSON
    private byte[] imageData;

    // Timestamps
    private String createdAt;

    // Default constructor
    public EquipmentResponse() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EquipmentCategory getCategory() {
        return category;
    }

    public void setCategory(EquipmentCategory category) {
        this.category = category;
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