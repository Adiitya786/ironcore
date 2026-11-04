package com.ironcore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for Gallery images.
 * Stores images uploaded for the gym gallery with category filtering.
 *
 * IMAGE STORAGE:
 * - imageName  → original filename (e.g. "gym-floor.jpg")
 * - imageType  → MIME type (e.g. "image/jpeg")
 * - imageData  → raw bytes stored as @Lob in database
 * Same pattern used by Equipment, Program, and Trainer entities.
 *
 * CATEGORIES (enum):
 * GYM | EQUIPMENT | TRAINERS | MEMBERS | EVENTS
 * Matches frontend filter tabs in Gallery.jsx
 */
@Entity
@Table(name = "gallery_images")
public class GalleryImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Image metadata =====

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GalleryCategory category;

    // ===== Image file storage (same as Trainer/Equipment) =====

    @Column(name = "image_name", length = 255)
    private String imageName;

    @Column(name = "image_type", length = 50)
    private String imageType;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    // ===== Display order (optional — for sorting gallery) =====

    @Column(name = "display_order")
    private Integer displayOrder;

    // ===== Timestamps =====

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ===== Lifecycle hooks =====

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public GalleryImageEntity() {}

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public GalleryCategory getCategory() { return category; }
    public void setCategory(GalleryCategory category) { this.category = category; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
