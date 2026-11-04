package com.ironcore.controller;

import com.ironcore.dto.ProgramRequest;
import com.ironcore.dto.ProgramResponse;
import com.ironcore.service.ProgramService;
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
 * REST Controller for Program operations.
 * Handles HTTP requests and responses for program API.
 * Supports image file uploads which are stored as byte data in the database.
 */
@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    // Logger for debugging
    private static final Logger logger = LoggerFactory.getLogger(ProgramController.class);

    // Service layer - injected by Spring
    private final ProgramService programService;

    // Constructor - Spring injects ProgramService
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    /**
     * Get all programs.
     * Method: GET
     * URL: /api/programs
     * Returns all programs from database with their image data.
     */
    @GetMapping
    public ResponseEntity<List<ProgramResponse>> getAllPrograms() {
        logger.info("GET /api/programs - Fetching all programs");

        try {
            // Call service to get all programs
            List<ProgramResponse> programs = programService.getAll();

            logger.info("Returning {} programs", programs.size());

            // Return 200 OK with list of programs
            return ResponseEntity.ok(programs);

        } catch (Exception e) {
            logger.error("Error fetching programs: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a single program by ID.
     * Method: GET
     * URL: /api/programs/{id}
     * Returns program details with image data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponse> getProgramById(@PathVariable Long id) {
        logger.info("GET /api/programs/{} - Fetching program by id", id);

        try {
            // Call service to get program by ID
            ProgramResponse program = programService.getById(id);

            // Return 200 OK with program
            return ResponseEntity.ok(program);

        } catch (Exception e) {
            logger.error("Error fetching program with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Create a new program with optional image upload.
     * Method: POST
     * URL: /api/programs
     * Content-Type: multipart/form-data
     *
     * Parameters:
     *   - title (required): Program title
     *   - description (optional): Program description
     *   - imageFile (optional): Image file to upload (jpg, png, etc.)
     *   - benefits (optional): Program benefits
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProgramResponse> createProgram(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) String benefits) {

        logger.info("POST /api/programs - Creating new program");
        logger.info("Title: {}", title);
        logger.info("Description: {}", description);
        logger.info("Benefits: {}", benefits);

        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Image file provided: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());
        } else {
            logger.info("No image file provided");
        }

        try {
            // Create ProgramRequest object
            ProgramRequest request = new ProgramRequest();

            // Set title (required)
            request.setTitle(title);

            // Set description (optional)
            if (description != null && !description.isEmpty()) {
                request.setDescription(description);
            }

            // Set benefits (optional)
            if (benefits != null && !benefits.isEmpty()) {
                request.setBenefits(benefits);
            }

            // Call service to create program (with optional image file)
            ProgramResponse createdProgram = programService.create(request, imageFile);

            logger.info("Program created successfully with id: {}", createdProgram.getId());

            // Return 201 Created with the new program
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProgram);

        } catch (IOException e) {
            logger.error("Error reading image file: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error creating program: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update an existing program with optional new image.
     * Method: PUT
     * URL: /api/programs/{id}
     * Content-Type: multipart/form-data
     *
     * Parameters:
     *   - id (required): Program ID to update
     *   - title (required): Updated program title
     *   - description (optional): Updated program description
     *   - imageFile (optional): New image file to upload
     *   - benefits (optional): Updated program benefits
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProgramResponse> updateProgram(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) String benefits) {

        logger.info("PUT /api/programs/{} - Updating program", id);
        logger.info("New title: {}", title);

        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("New image file provided: {} ({} bytes)", imageFile.getOriginalFilename(), imageFile.getSize());
        } else {
            logger.info("No new image file provided");
        }

        try {
            // Create ProgramRequest object
            ProgramRequest request = new ProgramRequest();

            // Set title
            if (title != null && !title.isEmpty()) {
                request.setTitle(title);
            }

            // Set description (optional)
            if (description != null && !description.isEmpty()) {
                request.setDescription(description);
            }

            // Set benefits (optional)
            if (benefits != null && !benefits.isEmpty()) {
                request.setBenefits(benefits);
            }

            // Call service to update program (with optional new image)
            ProgramResponse updatedProgram = programService.update(id, request, imageFile);

            logger.info("Program updated successfully: {}", updatedProgram.getTitle());

            // Return 200 OK with updated program
            return ResponseEntity.ok(updatedProgram);

        } catch (IOException e) {
            logger.error("Error reading image file: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error updating program with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete a program by ID.
     * Method: DELETE
     * URL: /api/programs/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        logger.info("DELETE /api/programs/{} - Deleting program", id);

        try {
            programService.delete(id);
            logger.info("Program with id {} deleted successfully", id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            logger.error("Program not found with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting program with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}