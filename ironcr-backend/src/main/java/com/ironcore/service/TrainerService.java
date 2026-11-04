package com.ironcore.service;

import com.ironcore.dto.TrainerRequest;
import com.ironcore.dto.TrainerResponse;
import com.ironcore.entity.Trainer;
import com.ironcore.repository.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Trainer operations.
 * Contains all business logic for creating, reading, updating, and deleting trainers.
 * Handles image file uploads and converts them to byte arrays for database storage.
 *
 * CRUD Operations:
 * - CREATE: Add new trainer with optional image
 * - READ: Get all trainers or single trainer by ID
 * - UPDATE: Update trainer details and optional image
 * - DELETE: Remove trainer from database
 */
@Service
@Transactional // Ensures all database operations run within a transaction (required for PostgreSQL LOB / byte[] streams)
public class TrainerService {

    // Logger for debugging and tracking
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    // Repository for database operations
    private final TrainerRepository repository;

    // Constructor - Spring automatically injects the repository
    public TrainerService(TrainerRepository repository) {
        this.repository = repository;
    }

    // ===================================================================
    //  READ OPERATIONS - Get trainers
    // ===================================================================

    /**
     * Get ALL trainers from database.
     * Returns list of all trainers.
     */
    @Transactional(readOnly = true)
    public List<TrainerResponse> getAll() {
        logger.info("Fetching all trainers from database");

        // Get all trainers from database
        List<Trainer> trainers = repository.findAll();

        logger.info("Found {} trainers", trainers.size());

        // Convert each Trainer entity to TrainerResponse
        List<TrainerResponse> responses = new ArrayList<>();
        for (Trainer trainer : trainers) {
            TrainerResponse response = convertToResponse(trainer);
            responses.add(response);
        }

        return responses;
    }

    /**
     * Get a single trainer by its ID.
     * Throws exception if trainer not found.
     */
    @Transactional(readOnly = true)
    public TrainerResponse getById(Long id) {
        logger.info("Fetching trainer with id: {}", id);

        // Find trainer by ID - throws exception if not found
        Trainer trainer = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Trainer not found with id: {}", id);
                return new RuntimeException("Trainer not found with id: " + id);
            });

        logger.info("Found trainer: {}", trainer.getName());

        return convertToResponse(trainer);
    }

    // ===================================================================
    //  CREATE OPERATION - Add new trainer with image
    // ===================================================================

    /**
     * Create a new trainer with optional image file.
     * Accepts trainer details and image file, converts image to bytes, saves to database.
     *
     * @param request   Trainer details (name, bio)
     * @param imageFile Optional image file (MultipartFile)
     * @return Created trainer response
     */
    public TrainerResponse create(TrainerRequest request, MultipartFile imageFile) throws IOException {
        logger.info("Creating new trainer: {}", request.getName());

        // Create new Trainer entity
        Trainer trainer = new Trainer();

        // Set name from request
        trainer.setName(request.getName());
        logger.info("Name set to: {}", request.getName());

        // Set bio from request (can be null)
        if (request.getBio() != null) {
            trainer.setBio(request.getBio());
        }

        // Handle image file upload if provided (same logic as Program and Equipment)
        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Processing image file: {}", imageFile.getOriginalFilename());

            // Get original filename
            String originalFilename = imageFile.getOriginalFilename();
            trainer.setImageName(originalFilename);
            logger.info("Image name stored: {}", originalFilename);

            // Get content type (MIME type) - e.g., "image/jpeg", "image/png"
            String contentType = imageFile.getContentType();
            trainer.setImageType(contentType);
            logger.info("Image type stored: {}", contentType);

            // Convert image file to byte array
            byte[] imageBytes = imageFile.getBytes();
            trainer.setImageData(imageBytes);
            logger.info("Image data converted to bytes: {} bytes", imageBytes.length);
        } else {
            logger.info("No image file provided");
        }

        // Save to database
        Trainer savedTrainer = repository.save(trainer);
        logger.info("Trainer saved with id: {}", savedTrainer.getId());

        // Return converted response
        return convertToResponse(savedTrainer);
    }

    // ===================================================================
    //  UPDATE OPERATION - Update existing trainer
    // ===================================================================

    /**
     * Update an existing trainer with optional new image.
     * Finds trainer by ID, updates its details, and optionally updates image.
     *
     * @param id        Trainer ID
     * @param request   Updated trainer details
     * @param imageFile Optional new image file
     * @return Updated trainer response
     */
    public TrainerResponse update(Long id, TrainerRequest request, MultipartFile imageFile) throws IOException {
        logger.info("Updating trainer with id: {}", id);

        // Find existing trainer - throws exception if not found
        Trainer trainer = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Trainer not found with id: {}", id);
                return new RuntimeException("Trainer not found with id: " + id);
            });

        // Update name
        if (request.getName() != null) {
            trainer.setName(request.getName());
            logger.info("Name updated to: {}", request.getName());
        }

        // Update bio
        if (request.getBio() != null) {
            trainer.setBio(request.getBio());
        }

        // Handle new image file upload if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Updating image file: {}", imageFile.getOriginalFilename());

            // Update image name
            String originalFilename = imageFile.getOriginalFilename();
            trainer.setImageName(originalFilename);
            logger.info("Image name updated to: {}", originalFilename);

            // Update image type
            String contentType = imageFile.getContentType();
            trainer.setImageType(contentType);
            logger.info("Image type updated to: {}", contentType);

            // Convert new image file to byte array
            byte[] imageBytes = imageFile.getBytes();
            trainer.setImageData(imageBytes);
            logger.info("Image data updated: {} bytes", imageBytes.length);
        } else {
            logger.info("No new image file provided - keeping existing image");
        }

        // Save updated trainer
        Trainer updatedTrainer = repository.save(trainer);
        logger.info("Trainer updated successfully: {}", updatedTrainer.getName());

        return convertToResponse(updatedTrainer);
    }

    // ===================================================================
    //  DELETE OPERATION - Remove trainer
    // ===================================================================

    /**
     * Delete a trainer by its ID.
     * Throws exception if trainer not found.
     *
     * @param id Trainer ID to delete
     */
    public void delete(Long id) {
        logger.info("Deleting trainer with id: {}", id);

        // Check if trainer exists before deleting
        Trainer trainer = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Trainer not found with id: {}", id);
                return new RuntimeException("Trainer not found with id: " + id);
            });

        // Delete from database
        repository.delete(trainer);
        logger.info("Trainer deleted successfully: {}", trainer.getName());
    }

    // ===================================================================
    //  HELPER METHOD - Convert entity to response DTO
    // ===================================================================

    /**
     * Convert Trainer entity to TrainerResponse DTO.
     * Maps all fields from entity to response object.
     * Image bytes will be automatically base64-encoded by Spring when converting to JSON.
     *
     * @param trainer Trainer entity to convert
     * @return Trainer response DTO
     */
    private TrainerResponse convertToResponse(Trainer trainer) {
        // Create new response object
        TrainerResponse response = new TrainerResponse();

        // Copy all fields from entity to response
        response.setId(trainer.getId());
        response.setName(trainer.getName());
        response.setBio(trainer.getBio());
        response.setImageName(trainer.getImageName());
        response.setImageType(trainer.getImageType());
        response.setImageData(trainer.getImageData());

        // Format timestamp as string for clean JSON output
        if (trainer.getCreatedAt() != null) {
            response.setCreatedAt(trainer.getCreatedAt().toString());
        }

        return response;
    }
}