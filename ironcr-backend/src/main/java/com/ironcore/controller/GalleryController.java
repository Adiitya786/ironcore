package com.ironcore.controller;

import com.ironcore.dto.GalleryImageResponse;
import com.ironcore.service.GalleryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * GALLERY REST CONTROLLER
 * =======================
 * Full CRUD API for Gallery images with category filtering and image uploads.
 *
 * BASE URL: /api/gallery
 *
 * ===========================================================
 *  AVAILABLE ENDPOINTS:
 * ===========================================================
 *
 *  GET    /api/gallery                         → Get ALL gallery images ("All" tab)
 *  GET    /api/gallery/category/{category}     → Get images by category (GYM, EQUIPMENT, TRAINERS, MEMBERS, EVENTS)
 *  GET    /api/gallery/{id}                    → Get single image by ID
 *  POST   /api/gallery                         → Upload new image with file (multipart/form-data)
 *  PUT    /api/gallery/{id}                    → Update image metadata or file (multipart/form-data)
 *  DELETE /api/gallery/{id}                    → Delete image (admin)
 *
 * ===========================================================
 *  HOW FRONTEND USES THIS:
 * ===========================================================
 *  - "All" tab       → GET /api/gallery
 *  - "Gym" tab       → GET /api/gallery/category/GYM
 *  - "Equipment" tab → GET /api/gallery/category/EQUIPMENT
 *  - "Trainers" tab  → GET /api/gallery/category/TRAINERS
 *  - "Members" tab   → GET /api/gallery/category/MEMBERS
 *  - "Events" tab    → GET /api/gallery/category/EVENTS
 */
@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private static final Logger log = LoggerFactory.getLogger(GalleryController.class);
    private final GalleryService service;

    public GalleryController(GalleryService service) {
        this.service = service;
    }

    // ===================================================================
    //  GET ALL GALLERY IMAGES ("All" Tab)
    // ===================================================================

    @GetMapping
    public ResponseEntity<List<GalleryImageResponse>> getAllImages() {
        log.info("GET /api/gallery - Fetching all gallery images");
        return ResponseEntity.ok(service.getAll());
    }

    // ===================================================================
    //  GET BY CATEGORY (Gym, Equipment, Trainers, Members, Events tabs)
    // ===================================================================

    @GetMapping("/category/{category}")
    public ResponseEntity<List<GalleryImageResponse>> getImagesByCategory(
            @PathVariable String category) {
        log.info("GET /api/gallery/category/{} - Fetching by category", category);
        return ResponseEntity.ok(service.getByCategory(category));
    }

    // ===================================================================
    //  GET SINGLE IMAGE BY ID
    // ===================================================================

    @GetMapping("/{id}")
    public ResponseEntity<GalleryImageResponse> getImageById(@PathVariable Long id) {
        log.info("GET /api/gallery/{} - Fetching single image", id);
        return ResponseEntity.ok(service.getById(id));
    }

    // ===================================================================
    //  UPLOAD NEW IMAGE (POST multipart/form-data)
    // ===================================================================

    /**
     * Upload new image with category.
     * Content-Type: multipart/form-data
     *
     * Params:
     *   - title        (required): Image title or caption
     *   - category     (required): GYM, EQUIPMENT, TRAINERS, MEMBERS, EVENTS
     *   - description  (optional): Details
     *   - displayOrder (optional): Sort sequence (integer)
     *   - imageFile    (required): Image file upload (jpg/png)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryImageResponse> uploadImage(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer displayOrder,
            @RequestParam("imageFile") MultipartFile imageFile) {

        log.info("POST /api/gallery | title={}, category={}", title, category);

        try {
            GalleryImageResponse created = service.uploadImage(
                title, category, description, displayOrder, imageFile
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (IOException e) {
            log.error("Failed to read image file during upload", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error uploading gallery image", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ===================================================================
    //  UPDATE IMAGE (PUT multipart/form-data)
    // ===================================================================

    /**
     * Update existing gallery image metadata or replace image file.
     * Content-Type: multipart/form-data
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryImageResponse> updateImage(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer displayOrder,
            @RequestParam(required = false) MultipartFile imageFile) {

        log.info("PUT /api/gallery/{} | updating metadata and/or image", id);

        try {
            GalleryImageResponse updated = service.updateImage(
                id, title, category, description, displayOrder, imageFile
            );
            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            log.error("Failed to read replacement image file", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error updating gallery image {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ===================================================================
    //  DELETE IMAGE
    // ===================================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        log.info("DELETE /api/gallery/{} - Deleting gallery image", id);
        service.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
