package com.ironcore.service;

import com.ironcore.dto.GalleryImageResponse;
import com.ironcore.entity.GalleryCategory;
import com.ironcore.entity.GalleryImageEntity;
import com.ironcore.repository.GalleryImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Gallery operations.
 * Handles all business logic: upload, filter, update, delete.
 *
 * IMAGE HANDLING (same as Trainer/Equipment/Program):
 * - Image is received as MultipartFile from controller
 * - Converted to byte[] using imageFile.getBytes()
 * - Stored as @Lob in DB alongside imageName and imageType
 * - On read, Spring auto base64-encodes the byte[] in JSON response
 *
 * TRANSACTION:
 * @Transactional on class level (required for @Lob / byte[] in PostgreSQL).
 * Read-only operations use @Transactional(readOnly = true) for performance.
 */
@Service
@Transactional
public class GalleryService {

    private static final Logger log = LoggerFactory.getLogger(GalleryService.class);
    private final GalleryImageRepository repository;

    public GalleryService(GalleryImageRepository repository) {
        this.repository = repository;
    }

    // ===================================================================
    //  READ OPERATIONS
    // ===================================================================

    /**
     * Get ALL gallery images sorted by display order.
     * Used for the "All" tab in frontend Gallery.jsx
     */
    @Transactional(readOnly = true)
    public List<GalleryImageResponse> getAll() {
        log.info("Fetching all gallery images");

        List<GalleryImageEntity> entities = repository.findAllByOrderByDisplayOrderAsc();
        log.info("Found {} gallery images", entities.size());

        List<GalleryImageResponse> responses = new ArrayList<>();
        for (GalleryImageEntity entity : entities) {
            responses.add(GalleryImageResponse.fromEntity(entity));
        }
        return responses;
    }

    /**
     * Get gallery images filtered by category.
     * Frontend filter tabs: Gym → GYM, Equipment → EQUIPMENT, etc.
     *
     * @param categoryStr - "GYM", "EQUIPMENT", "TRAINERS", "MEMBERS", "EVENTS"
     */
    @Transactional(readOnly = true)
    public List<GalleryImageResponse> getByCategory(String categoryStr) {
        log.info("Fetching gallery images for category: {}", categoryStr);

        // ===== STEP 1: Convert string to enum =====
        GalleryCategory category;
        try {
            category = GalleryCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid category: {}. Must be: GYM, EQUIPMENT, TRAINERS, MEMBERS, EVENTS", categoryStr);
            throw new RuntimeException("Invalid category: " + categoryStr +
                ". Valid values: GYM, EQUIPMENT, TRAINERS, MEMBERS, EVENTS");
        }

        // ===== STEP 2: Query database =====
        List<GalleryImageEntity> entities = repository.findByCategoryOrderByDisplayOrderAsc(category);
        log.info("Found {} images for category: {}", entities.size(), categoryStr);

        // ===== STEP 3: Convert to response DTOs =====
        List<GalleryImageResponse> responses = new ArrayList<>();
        for (GalleryImageEntity entity : entities) {
            responses.add(GalleryImageResponse.fromEntity(entity));
        }
        return responses;
    }

    /**
     * Get single gallery image by ID.
     */
    @Transactional(readOnly = true)
    public GalleryImageResponse getById(Long id) {
        log.info("Fetching gallery image with id: {}", id);

        GalleryImageEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                log.error("Gallery image not found with id: {}", id);
                return new RuntimeException("Gallery image not found with id: " + id);
            });

        return GalleryImageResponse.fromEntity(entity);
    }

    // ===================================================================
    //  CREATE OPERATION — Upload new image
    // ===================================================================

    /**
     * Upload a new gallery image with metadata.
     *
     * TRANSACTION FLOW:
     * 1. Validate category string → convert to enum
     * 2. Create new entity
     * 3. Map title, description, displayOrder
     * 4. Convert image file to bytes and store: imageName, imageType, imageData
     * 5. Save to database (ALL OR NOTHING — rolls back if anything fails)
     * 6. Return saved entity as response DTO
     *
     * @param title        - Image title (required)
     * @param categoryStr  - Category string: GYM/EQUIPMENT/TRAINERS/MEMBERS/EVENTS (required)
     * @param description  - Optional description
     * @param displayOrder - Optional sort order (default 0)
     * @param imageFile    - Image file (required — MultipartFile)
     */
    public GalleryImageResponse uploadImage(
            String title,
            String categoryStr,
            String description,
            Integer displayOrder,
            MultipartFile imageFile) throws IOException {

        log.info("Uploading new gallery image: title={}, category={}", title, categoryStr);

        // ===== STEP 1: Validate and convert category =====
        GalleryCategory category;
        try {
            category = GalleryCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid category: {}", categoryStr);
            throw new RuntimeException("Invalid category: " + categoryStr +
                ". Valid values: GYM, EQUIPMENT, TRAINERS, MEMBERS, EVENTS");
        }

        // ===== STEP 2: Create new entity =====
        GalleryImageEntity entity = new GalleryImageEntity();

        // ===== STEP 3: Map text fields =====
        entity.setTitle(title);
        entity.setCategory(category);

        if (description != null && !description.isBlank()) {
            entity.setDescription(description);
        }
        if (displayOrder != null) {
            entity.setDisplayOrder(displayOrder);
        }

        // ===== STEP 4: Process image file (same as Trainer/Equipment) =====
        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("Processing image: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());

            entity.setImageName(imageFile.getOriginalFilename());      // original filename
            entity.setImageType(imageFile.getContentType());           // MIME type
            entity.setImageData(imageFile.getBytes());                 // raw bytes → @Lob in DB
        } else {
            log.warn("No image file provided for gallery upload — imageData will be null");
        }

        // ===== STEP 5: Save to database (ALL OR NOTHING) =====
        GalleryImageEntity saved = repository.save(entity);
        log.info("Gallery image saved with id: {}", saved.getId());

        // ===== STEP 6: Return response =====
        return GalleryImageResponse.fromEntity(saved);
    }

    // ===================================================================
    //  UPDATE OPERATION — Change metadata or swap image
    // ===================================================================

    /**
     * Update an existing gallery image.
     * All parameters are optional except id — only provided fields are updated.
     * If imageFile is provided, the image bytes are replaced (old image deleted).
     *
     * TRANSACTION FLOW:
     * 1. Find entity by ID (throws if not found)
     * 2. Update only the provided fields
     * 3. If new imageFile provided → replace imageData, imageName, imageType
     * 4. Save changes (ALL OR NOTHING)
     */
    public GalleryImageResponse updateImage(
            Long id,
            String title,
            String categoryStr,
            String description,
            Integer displayOrder,
            MultipartFile imageFile) throws IOException {

        log.info("Updating gallery image with id: {}", id);

        // ===== STEP 1: Find existing entity =====
        GalleryImageEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                log.error("Gallery image not found with id: {}", id);
                return new RuntimeException("Gallery image not found with id: " + id);
            });

        // ===== STEP 2: Update only provided fields =====
        if (title != null && !title.isBlank()) {
            entity.setTitle(title);
            log.debug("Title updated to: {}", title);
        }
        if (description != null) {
            entity.setDescription(description);
        }
        if (displayOrder != null) {
            entity.setDisplayOrder(displayOrder);
        }
        if (categoryStr != null && !categoryStr.isBlank()) {
            try {
                entity.setCategory(GalleryCategory.valueOf(categoryStr.toUpperCase()));
                log.debug("Category updated to: {}", categoryStr);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid category: " + categoryStr);
            }
        }

        // ===== STEP 3: Replace image if new file provided =====
        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("Replacing image with: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());

            entity.setImageName(imageFile.getOriginalFilename());
            entity.setImageType(imageFile.getContentType());
            entity.setImageData(imageFile.getBytes());
        } else {
            log.info("No new image provided — keeping existing image");
        }

        // ===== STEP 4: Save changes (ALL OR NOTHING) =====
        GalleryImageEntity saved = repository.save(entity);
        log.info("Gallery image {} updated successfully", id);

        return GalleryImageResponse.fromEntity(saved);
    }

    // ===================================================================
    //  DELETE OPERATION
    // ===================================================================

    /**
     * Delete a gallery image by ID.
     * Throws exception if not found.
     */
    public void deleteImage(Long id) {
        log.info("Deleting gallery image with id: {}", id);

        // ===== STEP 1: Check if exists =====
        GalleryImageEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                log.error("Gallery image not found with id: {}", id);
                return new RuntimeException("Gallery image not found with id: " + id);
            });

        // ===== STEP 2: Delete (ALL OR NOTHING) =====
        repository.delete(entity);
        log.info("Gallery image {} ({}) deleted successfully", id, entity.getTitle());
    }
}
