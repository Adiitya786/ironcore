package com.ironcore.dto;

import com.ironcore.entity.EquipmentCategory;

/**
 * Data Transfer Object for Equipment request.
 * Used to receive equipment data from client.
 */
public class EquipmentRequest {

    // Equipment name - required
    private String name;

    // Equipment description
    private String description;

    // Equipment category - STRENGTH, CARDIO, or OTHER
    private EquipmentCategory category;

    // Image filename (for update)
    private String imageName;

    // Default constructor
    public EquipmentRequest() {}

    // ===== Getter and Setter Methods =====

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
}