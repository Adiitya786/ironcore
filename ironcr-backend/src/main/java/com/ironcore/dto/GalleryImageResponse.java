package com.ironcore.dto;

import com.ironcore.entity.GalleryImageEntity;

/**
 * Response DTO for a Gallery image.
 * Returned by all GET endpoints — includes image bytes, category, and metadata.
 *
 * NOTE ON imageData:
 * The byte[] field is automatically base64-encoded by Spring/Jackson when
 * serialized to JSON. Frontend decodes it with:
 *   <img src={`data:${image.imageType};base64,${image.imageData}`} />
 */
public class GalleryImageResponse {

    private Long id;
    private String title;
    private String description;
    private String category;      // "GYM", "EQUIPMENT", "TRAINERS", "MEMBERS", "EVENTS"
    private String imageName;     // original filename
    private String imageType;     // MIME type: "image/jpeg", "image/png"
    private byte[] imageData;     // raw bytes — Spring base64-encodes this in JSON
    private Integer displayOrder;
    private String createdAt;

    // ===== Factory method: Entity → DTO =====

    public static GalleryImageResponse fromEntity(GalleryImageEntity entity) {
        GalleryImageResponse dto = new GalleryImageResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().name() : null);
        dto.setImageName(entity.getImageName());
        dto.setImageType(entity.getImageType());
        dto.setImageData(entity.getImageData());
        dto.setDisplayOrder(entity.getDisplayOrder());
        dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        return dto;
    }

    // ===== Getters and Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
