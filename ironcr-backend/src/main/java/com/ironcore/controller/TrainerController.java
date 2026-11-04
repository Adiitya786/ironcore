package com.ironcore.controller;

import com.ironcore.dto.TrainerRequest;
import com.ironcore.dto.TrainerResponse;
import com.ironcore.service.TrainerService;
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
 * REST Controller for Trainer operations.
 * Handles HTTP requests and responses for trainer API.
 * Supports image file uploads stored as byte data in the database.
 *
 * BASE URL: /api/trainers
 *
 * ===========================================================
 *  AVAILABLE ENDPOINTS:
 * ===========================================================
 *
 *  GET    /api/trainers              → Get all trainers
 *  GET    /api/trainers/{id}         → Get single trainer by ID
 *  POST   /api/trainers              → Create new trainer (with image)
 *  PUT    /api/trainers/{id}         → Update trainer (with optional new image)
 *  DELETE /api/trainers/{id}         → Delete trainer
 *
 * ===========================================================
 *  IMAGE HANDLING:
 * ===========================================================
 *
 *  - Images are stored as binary byte[] in the database
 *  - Uploaded as multipart/form-data with key "imageFile"
 *  - Returned in JSON as base64-encoded string
 *  - Frontend decodes: data:${trainer.imageType};base64,${trainer.imageData}
 *
 */
@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    // Logger for debugging
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    // Service layer - injected by Spring
    private final TrainerService trainerService;

    // Constructor - Spring injects TrainerService
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // =======================================================================
    //  GET ALL TRAINERS
    // =======================================================================

    /**
     * Get all trainers.
     * Method: GET
     * URL: /api/trainers
     * Returns all trainers from database with their image data.
     */
    @GetMapping
    public ResponseEntity<List<TrainerResponse>> getAllTrainers() {
        logger.info("GET /api/trainers - Fetching all trainers");

        try {
            // Call service to get all trainers
            List<TrainerResponse> trainers = trainerService.getAll();

            logger.info("Returning {} trainers", trainers.size());

            // Return 200 OK with list of trainers
            return ResponseEntity.ok(trainers);

        } catch (Exception e) {
            logger.error("Error fetching trainers: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =======================================================================
    //  GET SINGLE TRAINER BY ID
    // =======================================================================

    /**
     * Get a single trainer by ID.
     * Method: GET
     * URL: /api/trainers/{id}
     * Returns trainer details with image data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainerResponse> getTrainerById(@PathVariable Long id) {
        logger.info("GET /api/trainers/{} - Fetching trainer by id", id);

        try {
            // Call service to get trainer by ID
            TrainerResponse trainer = trainerService.getById(id);

            // Return 200 OK with trainer
            return ResponseEntity.ok(trainer);

        } catch (Exception e) {
            logger.error("Error fetching trainer with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // =======================================================================
    //  CREATE NEW TRAINER (with image upload)
    // =======================================================================

    /**
     * Create a new trainer with optional image upload.
     * Method: POST
     * URL: /api/trainers
     * Content-Type: multipart/form-data
     *
     * Parameters (all sent as form-data):
     *   - name        (required): Trainer name
     *   - bio         (optional): Trainer bio/description
     *   - imageFile   (optional): Image file to upload (jpg, png, etc.)
     *
     * Example in Postman:
     *   Key: name          | Type: Text | Value: John Doe
     *   Key: bio           | Type: Text | Value: Certified fitness trainer
     *   Key: imageFile     | Type: File | Select trainer.jpg
     *
     * Response (201 Created):
     *   {
     *     "id": 1,
     *     "name": "John Doe",
     *     "bio": "Certified fitness trainer",
     *     "imageName": "trainer.jpg",
     *     "imageType": "image/jpeg",
     *     "imageData": "/9j/4AAQSkZJRgABAQEASABIAAD...",
     *     "createdAt": "2026-11-02T12:25:20"
     *   }
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrainerResponse> createTrainer(
            @RequestParam String name,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) MultipartFile imageFile) {

        logger.info("POST /api/trainers - Creating new trainer");
        logger.info("Name: {}", name);
        logger.info("Bio: {}", bio);

        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Image file provided: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());
        } else {
            logger.info("No image file provided");
        }

        try {
            // Create TrainerRequest object
            TrainerRequest request = new TrainerRequest();

            // Set name (required)
            request.setName(name);

            // Set bio (optional)
            if (bio != null && !bio.isEmpty()) {
                request.setBio(bio);
            }

            // Call service to create trainer (with optional image file)
            TrainerResponse createdTrainer = trainerService.create(request, imageFile);

            logger.info("Trainer created successfully with id: {}", createdTrainer.getId());

            // Return 201 Created with the new trainer
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainer);

        } catch (IOException e) {
            logger.error("Error reading image file: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error creating trainer: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // =======================================================================
    //  UPDATE EXISTING TRAINER (with optional new image)
    // =======================================================================

    /**
     * Update an existing trainer with optional new image.
     * Method: PUT
     * URL: /api/trainers/{id}
     * Content-Type: multipart/form-data
     *
     * Parameters (all sent as form-data):
     *   - name        (optional): Updated trainer name
     *   - bio         (optional): Updated bio/description
     *   - imageFile   (optional): New image file (if not provided, keeps existing image)
     *
     * Example in Postman:
     *   Key: name          | Type: Text | Value: John Doe Updated
     *   Key: bio           | Type: Text | Value: Senior fitness trainer
     *   Key: imageFile     | Type: File | Select new_trainer.jpg
     *
     * Response (200 OK):
     *   {
     *     "id": 1,
     *     "name": "John Doe Updated",
     *     "bio": "Senior fitness trainer",
     *     "imageName": "new_trainer.jpg",
     *     "imageType": "image/jpeg",
     *     "imageData": "/9j/4AAQSkZJRgABAQEASABIAAD...",
     *     "createdAt": "2026-11-02T12:25:20"
     *   }
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrainerResponse> updateTrainer(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) MultipartFile imageFile) {

        logger.info("PUT /api/trainers/{} - Updating trainer", id);

        if (name != null) {
            logger.info("New name: {}", name);
        }
        if (bio != null) {
            logger.info("New bio: {}", bio);
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("New image file provided: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());
        } else {
            logger.info("No new image file - existing image will be kept");
        }

        try {
            // Create TrainerRequest object with only fields that were provided
            TrainerRequest request = new TrainerRequest();

            if (name != null && !name.isEmpty()) {
                request.setName(name);
            }

            if (bio != null && !bio.isEmpty()) {
                request.setBio(bio);
            }

            // Call service to update trainer (with optional new image)
            TrainerResponse updatedTrainer = trainerService.update(id, request, imageFile);

            logger.info("Trainer updated successfully: {}", updatedTrainer.getName());

            // Return 200 OK with updated trainer
            return ResponseEntity.ok(updatedTrainer);

        } catch (IOException e) {
            logger.error("Error reading image file: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error updating trainer with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // =======================================================================
    //  DELETE TRAINER
    // =======================================================================

    /**
     * Delete a trainer by ID.
     * Method: DELETE
     * URL: /api/trainers/{id}
     *
     * Response (204 No Content):
     *   No body returned on success
     *
     * Example in Postman:
     *   DELETE http://localhost:8080/api/trainers/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
        logger.info("DELETE /api/trainers/{} - Deleting trainer", id);

        try {
            // Call service to delete trainer
            trainerService.delete(id);

            logger.info("Trainer with id {} deleted successfully", id);

            // Return 204 No Content (success, no body)
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            logger.error("Error deleting trainer with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}