package com.ironcore.service;

import com.ironcore.dto.EquipmentRequest;
import com.ironcore.dto.EquipmentResponse;
import com.ironcore.entity.Equipment;
import com.ironcore.entity.EquipmentCategory;
import com.ironcore.repository.EquipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer for Equipment operations.
 * Contains all business logic for creating, reading, updating, and deleting equipment.
 * Handles image file uploads and converts them to byte arrays for database storage.
 */
@Service
@Transactional // Ensures all database operations run within a transaction (required for PostgreSQL LOB / byte[] streams)
public class EquipmentService {

    // Logger for debugging and tracking
    private static final Logger logger = LoggerFactory.getLogger(EquipmentService.class);

    // Repository for database operations
    private final EquipmentRepository repository;

    // Constructor - Spring automatically injects the repository
    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }

    // ===================================================================
    //  READ OPERATIONS - Get equipment (all, by category, grouped)
    // ===================================================================

    /**
     * Get ALL equipment from database (default view).
     * No category filter applied - returns everything.
     * Equipment is sorted by category first, then by name.
     */
    @Transactional(readOnly = true)
    public List<EquipmentResponse> getAll() {
        logger.info("Fetching all equipment from database");

        // Get all equipment sorted by category and name
        List<Equipment> equipmentList = repository.findAllByOrderByCategoryAscNameAsc();

        logger.info("Found {} equipment items", equipmentList.size());

        // Convert each Equipment entity to EquipmentResponse
        List<EquipmentResponse> responses = new ArrayList<>();
        for (Equipment equipment : equipmentList) {
            EquipmentResponse response = convertToResponse(equipment);
            responses.add(response);
        }

        return responses;
    }

    /**
     * Get equipment filtered by a SPECIFIC CATEGORY.
     * Use this when user clicks a category tab (e.g., "Strength", "Cardio", "Other").
     *
     * @param category The category to filter by (STRENGTH, CARDIO, or OTHER)
     */
    @Transactional(readOnly = true)
    public List<EquipmentResponse> getByCategory(EquipmentCategory category) {
        logger.info("Fetching equipment for category: {}", category);

        // Query database for equipment matching this category
        List<Equipment> equipmentList = repository.findByCategory(category);

        logger.info("Found {} equipment items in category: {}", equipmentList.size(), category);

        // Convert each Equipment entity to EquipmentResponse
        List<EquipmentResponse> responses = new ArrayList<>();
        for (Equipment equipment : equipmentList) {
            EquipmentResponse response = convertToResponse(equipment);
            responses.add(response);
        }

        return responses;
    }

    /**
     * Get ALL equipment GROUPED BY CATEGORY.
     * Returns a map where:
     *   key   = category name (e.g., "STRENGTH", "CARDIO", "OTHER")
     *   value = list of equipment in that category
     */
    @Transactional(readOnly = true)
    public Map<String, List<EquipmentResponse>> getGroupedByCategory() {
        logger.info("Fetching all equipment grouped by category");

        // Get all equipment sorted by category and name
        List<Equipment> allEquipment = repository.findAllByOrderByCategoryAscNameAsc();

        logger.info("Total equipment found: {}", allEquipment.size());

        // Use LinkedHashMap to keep categories in a consistent order
        Map<String, List<EquipmentResponse>> groupedMap = new LinkedHashMap<>();

        // Initialize all categories (even if empty) so frontend always gets all tabs
        groupedMap.put("STRENGTH", new ArrayList<>());
        groupedMap.put("CARDIO", new ArrayList<>());
        groupedMap.put("OTHER", new ArrayList<>());

        // Loop through all equipment and sort into the correct category bucket
        for (Equipment equipment : allEquipment) {
            // Convert entity to response DTO
            EquipmentResponse response = convertToResponse(equipment);

            // Get the category name as a string (e.g., "STRENGTH")
            String categoryName = equipment.getCategory().name();

            // Add to the matching category list
            groupedMap.get(categoryName).add(response);
        }

        return groupedMap;
    }

    /**
     * Get a single equipment by its ID.
     * Throws exception if equipment not found.
     */
    @Transactional(readOnly = true)
    public EquipmentResponse getById(Long id) {
        logger.info("Fetching equipment with id: {}", id);

        // Find equipment by ID - throws exception if not found
        Equipment equipment = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Equipment not found with id: {}", id);
                return new RuntimeException("Equipment not found with id: " + id);
            });

        logger.info("Found equipment: {}", equipment.getName());

        return convertToResponse(equipment);
    }

    // ===================================================================
    //  CREATE OPERATION - Add new equipment with image
    // ===================================================================

    /**
     * Create a new equipment with optional image file.
     */
    public EquipmentResponse create(EquipmentRequest request, MultipartFile imageFile) throws IOException {
        logger.info("Creating new equipment: {}", request.getName());

        // Create new Equipment entity
        Equipment equipment = new Equipment();

        // Set name from request
        equipment.setName(request.getName());
        logger.info("Name set to: {}", request.getName());

        // Set description from request (can be null)
        if (request.getDescription() != null) {
            equipment.setDescription(request.getDescription());
        }

        // Set category from request (required)
        equipment.setCategory(request.getCategory());
        logger.info("Category set to: {}", request.getCategory());

        // Handle image file upload if provided (same logic as Program entity)
        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Processing image file: {}", imageFile.getOriginalFilename());

            // Get original filename
            String originalFilename = imageFile.getOriginalFilename();
            equipment.setImageName(originalFilename);
            logger.info("Image name stored: {}", originalFilename);

            // Get content type (MIME type) - e.g., "image/jpeg", "image/png"
            String contentType = imageFile.getContentType();
            equipment.setImageType(contentType);
            logger.info("Image type stored: {}", contentType);

            // Convert image file to byte array
            byte[] imageBytes = imageFile.getBytes();
            equipment.setImageData(imageBytes);
            logger.info("Image data converted to bytes: {} bytes", imageBytes.length);
        } else {
            logger.info("No image file provided");
        }

        // Save to database
        Equipment savedEquipment = repository.save(equipment);
        logger.info("Equipment saved with id: {}", savedEquipment.getId());

        // Return converted response
        return convertToResponse(savedEquipment);
    }

    // ===================================================================
    //  UPDATE OPERATION - Update existing equipment
    // ===================================================================

    /**
     * Update an existing equipment with optional new image.
     */
    public EquipmentResponse update(Long id, EquipmentRequest request, MultipartFile imageFile) throws IOException {
        logger.info("Updating equipment with id: {}", id);

        // Find existing equipment - throws exception if not found
        Equipment equipment = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Equipment not found with id: {}", id);
                return new RuntimeException("Equipment not found with id: " + id);
            });

        // Update name
        if (request.getName() != null) {
            equipment.setName(request.getName());
            logger.info("Name updated to: {}", request.getName());
        }

        // Update description
        if (request.getDescription() != null) {
            equipment.setDescription(request.getDescription());
        }

        // Update category
        if (request.getCategory() != null) {
            equipment.setCategory(request.getCategory());
            logger.info("Category updated to: {}", request.getCategory());
        }

        // Handle new image file upload if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Updating image file: {}", imageFile.getOriginalFilename());

            // Update image name
            String originalFilename = imageFile.getOriginalFilename();
            equipment.setImageName(originalFilename);
            logger.info("Image name updated to: {}", originalFilename);

            // Update image type
            String contentType = imageFile.getContentType();
            equipment.setImageType(contentType);
            logger.info("Image type updated to: {}", contentType);

            // Convert new image file to byte array
            byte[] imageBytes = imageFile.getBytes();
            equipment.setImageData(imageBytes);
            logger.info("Image data updated: {} bytes", imageBytes.length);
        } else {
            logger.info("No new image file provided - keeping existing image");
        }

        // Save updated equipment
        Equipment updatedEquipment = repository.save(equipment);
        logger.info("Equipment updated successfully: {}", updatedEquipment.getName());

        return convertToResponse(updatedEquipment);
    }

    // ===================================================================
    //  DELETE OPERATION - Remove equipment
    // ===================================================================

    /**
     * Delete an equipment by its ID.
     */
    public void delete(Long id) {
        logger.info("Deleting equipment with id: {}", id);

        // Check if equipment exists before deleting
        Equipment equipment = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Equipment not found with id: {}", id);
                return new RuntimeException("Equipment not found with id: " + id);
            });

        // Delete from database
        repository.delete(equipment);
        logger.info("Equipment deleted successfully: {}", equipment.getName());
    }

    // ===================================================================
    //  HELPER METHOD - Convert entity to response DTO
    // ===================================================================

    private EquipmentResponse convertToResponse(Equipment equipment) {
        EquipmentResponse response = new EquipmentResponse();

        response.setId(equipment.getId());
        response.setName(equipment.getName());
        response.setDescription(equipment.getDescription());
        response.setCategory(equipment.getCategory());
        response.setImageName(equipment.getImageName());
        response.setImageType(equipment.getImageType());
        response.setImageData(equipment.getImageData());

        if (equipment.getCreatedAt() != null) {
            response.setCreatedAt(equipment.getCreatedAt().toString());
        }

        return response;
    }
}