package com.ironcore.controller;

import com.ironcore.dto.EquipmentRequest;
import com.ironcore.dto.EquipmentResponse;
import com.ironcore.entity.EquipmentCategory;
import com.ironcore.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Equipment operations.
 * Handles HTTP requests and responses for equipment API.
 * Supports image file uploads stored as byte data in the database.
 *
 * BASE URL: /api/equipment
 *
 * ===========================================================
 *  AVAILABLE ENDPOINTS:
 * ===========================================================
 *
 *  GET    /api/equipment                    → All equipment (default: show all)
 *  GET    /api/equipment/grouped            → All equipment grouped by category
 *  GET    /api/equipment/category/STRENGTH  → Only STRENGTH equipment
 *  GET    /api/equipment/category/CARDIO    → Only CARDIO equipment
 *  GET    /api/equipment/category/OTHER     → Only OTHER equipment
 *  GET    /api/equipment/{id}               → Single equipment by ID
 *  POST   /api/equipment                    → Create new equipment (with image)
 *  PUT    /api/equipment/{id}               → Update equipment (with optional new image)
 *  DELETE /api/equipment/{id}               → Delete equipment
 *
 * ===========================================================
 *  HOW FRONTEND USES THIS:
 * ===========================================================
 *
 *  - "All" tab  → call GET /api/equipment           (returns all equipment)
 *  - "Strength" tab → call GET /api/equipment/category/STRENGTH
 *  - "Cardio" tab   → call GET /api/equipment/category/CARDIO
 *  - "Others" tab   → call GET /api/equipment/category/OTHER
 *
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    // Logger for debugging
    private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);

    // Service layer - injected by Spring
    private final EquipmentService equipmentService;

    // Constructor - Spring injects EquipmentService
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // =======================================================================
    //  GET ALL EQUIPMENT (Default - no category filter)
    // =======================================================================

    /**
     * Get ALL equipment (default view - no filter applied).
     * Method: GET
     * URL: /api/equipment
     *
     * Use: When user selects "All" tab in frontend
     */
    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment() {
        logger.info("GET /api/equipment - Fetching all equipment (no category filter)");

        try {
            List<EquipmentResponse> equipmentList = equipmentService.getAll();

            logger.info("Returning {} equipment items", equipmentList.size());

            // Return 200 OK with all equipment
            return ResponseEntity.ok(equipmentList);

        } catch (Exception e) {
            logger.error("Error fetching all equipment: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =======================================================================
    //  GET EQUIPMENT GROUPED BY CATEGORY (All categories in one response)
    // =======================================================================

    /**
     * Get ALL equipment grouped by category.
     * Method: GET
     * URL: /api/equipment/grouped
     *
     * Returns a map: { "STRENGTH": [...], "CARDIO": [...], "OTHER": [...] }
     *
     * Use: When frontend wants to show all tabs at once without multiple API calls
     */
    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<EquipmentResponse>>> getEquipmentGroupedByCategory() {
        logger.info("GET /api/equipment/grouped - Fetching equipment grouped by category");

        try {
            Map<String, List<EquipmentResponse>> groupedEquipment = equipmentService.getGroupedByCategory();

            logger.info("Returning equipment grouped in {} categories", groupedEquipment.size());

            // Return 200 OK with grouped equipment
            return ResponseEntity.ok(groupedEquipment);

        } catch (Exception e) {
            logger.error("Error fetching grouped equipment: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =======================================================================
    //  GET EQUIPMENT BY SPECIFIC CATEGORY
    // =======================================================================

    /**
     * Get equipment filtered by a SPECIFIC CATEGORY.
     * Method: GET
     * URL: /api/equipment/category/{category}
     *
     * Valid values for {category}: STRENGTH, CARDIO, OTHER
     *
     * Examples:
     *   GET /api/equipment/category/STRENGTH  → returns all strength equipment
     *   GET /api/equipment/category/CARDIO    → returns all cardio equipment
     *   GET /api/equipment/category/OTHER     → returns all other equipment
     *
     * Use: When user clicks a specific category tab in frontend
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<EquipmentResponse>> getEquipmentByCategory(
            @PathVariable String category) {

        logger.info("GET /api/equipment/category/{} - Fetching equipment by category", category);

        try {
            // Convert the URL string (e.g., "STRENGTH") to EquipmentCategory enum
            EquipmentCategory equipmentCategory = EquipmentCategory.valueOf(category.toUpperCase());

            // Get equipment for this category
            List<EquipmentResponse> equipmentList = equipmentService.getByCategory(equipmentCategory);

            logger.info("Returning {} items for category: {}", equipmentList.size(), category);

            // Return 200 OK with category-filtered equipment
            return ResponseEntity.ok(equipmentList);

        } catch (IllegalArgumentException e) {
            // This happens if an invalid category name is passed (e.g., "YOGA")
            logger.error("Invalid category value: {}. Must be STRENGTH, CARDIO, or OTHER", category);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Error fetching equipment for category {}: ", category, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =======================================================================
    //  GET SINGLE EQUIPMENT BY ID
    // =======================================================================

    /**
     * Get a single equipment item by its ID.
     * Method: GET
     * URL: /api/equipment/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Long id) {
        logger.info("GET /api/equipment/{} - Fetching equipment by id", id);

        try {
            EquipmentResponse equipment = equipmentService.getById(id);

            // Return 200 OK with equipment
            return ResponseEntity.ok(equipment);

        } catch (Exception e) {
            logger.error("Equipment not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // =======================================================================
    //  CREATE NEW EQUIPMENT (with image upload)
    // =======================================================================

    /**
     * Create a new equipment item with optional image upload.
     * Method: POST
     * URL: /api/equipment
     * Content-Type: multipart/form-data
     *
     * Parameters (all sent as form-data):
     *   - name        (required): Equipment name
     *   - category    (required): STRENGTH, CARDIO, or OTHER
     *   - description (optional): Equipment description
     *   - imageFile   (optional): Image file to upload (jpg, png, etc.)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EquipmentResponse> createEquipment(
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile imageFile) {

        logger.info("POST /api/equipment - Creating new equipment");
        logger.info("Name: {}", name);
        logger.info("Category: {}", category);
        logger.info("Description: {}", description);

        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Image file provided: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());
        } else {
            logger.info("No image file provided");
        }

        try {
            // Convert the category string to enum
            EquipmentCategory equipmentCategory = EquipmentCategory.valueOf(category.toUpperCase());

            // Build the EquipmentRequest object
            EquipmentRequest request = new EquipmentRequest();
            request.setName(name);
            request.setCategory(equipmentCategory);

            if (description != null && !description.isEmpty()) {
                request.setDescription(description);
            }

            // Call service to create equipment (image file handling happens there)
            EquipmentResponse createdEquipment = equipmentService.create(request, imageFile);

            logger.info("Equipment created successfully with id: {}", createdEquipment.getId());

            // Return 201 Created with the new equipment
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEquipment);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid category: {}. Must be STRENGTH, CARDIO, or OTHER", category);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (IOException e) {
            logger.error("Error reading image file: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Error creating equipment: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // =======================================================================
    //  UPDATE EXISTING EQUIPMENT (with optional new image)
    // =======================================================================

    /**
     * Update an existing equipment item with optional new image.
     * Method: PUT
     * URL: /api/equipment/{id}
     * Content-Type: multipart/form-data
     *
     * Parameters (all sent as form-data):
     *   - name        (optional): Updated equipment name
     *   - category    (optional): Updated category (STRENGTH, CARDIO, or OTHER)
     *   - description (optional): Updated description
     *   - imageFile   (optional): New image file (if not provided, keeps existing image)
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile imageFile) {

        logger.info("PUT /api/equipment/{} - Updating equipment", id);

        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("New image file provided: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());
        } else {
            logger.info("No new image file - existing image will be kept");
        }

        try {
            // Build the EquipmentRequest with only the fields that were provided
            EquipmentRequest request = new EquipmentRequest();

            if (name != null && !name.isEmpty()) {
                request.setName(name);
            }

            if (description != null && !description.isEmpty()) {
                request.setDescription(description);
            }

            if (category != null && !category.isEmpty()) {
                // Convert string to enum
                EquipmentCategory equipmentCategory = EquipmentCategory.valueOf(category.toUpperCase());
                request.setCategory(equipmentCategory);
            }

            // Call service to update equipment (with optional new image)
            EquipmentResponse updatedEquipment = equipmentService.update(id, request, imageFile);

            logger.info("Equipment updated successfully: {}", updatedEquipment.getName());

            // Return 200 OK with updated equipment
            return ResponseEntity.ok(updatedEquipment);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid category: {}. Must be STRENGTH, CARDIO, or OTHER", category);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (IOException e) {
            logger.error("Error reading image file: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {
            logger.error("Error updating equipment with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // =======================================================================
    //  DELETE EQUIPMENT
    // =======================================================================

    /**
     * Delete an equipment item by ID.
     * Method: DELETE
     * URL: /api/equipment/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        logger.info("DELETE /api/equipment/{} - Deleting equipment", id);

        try {
            equipmentService.delete(id);

            logger.info("Equipment with id {} deleted successfully", id);

            // Return 204 No Content (success, no body)
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            logger.error("Error deleting equipment with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}