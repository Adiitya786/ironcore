package com.ironcore.service;

import com.ironcore.dto.ProgramRequest;
import com.ironcore.dto.ProgramResponse;
import com.ironcore.entity.Program;
import com.ironcore.repository.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service layer for Program operations.
 * Contains all business logic for creating, reading, and updating programs.
 * Handles image file uploads and converts them to byte arrays for database storage.
 */
@Service
public class ProgramService {

    // Logger for debugging and tracking
    private static final Logger logger = LoggerFactory.getLogger(ProgramService.class);

    // Repository for database operations
    private final ProgramRepository repository;

    // Constructor - Spring automatically injects the repository
    public ProgramService(ProgramRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all programs from database.
     * Returns list of all programs.
     */
    public List<ProgramResponse> getAll() {
        logger.info("Fetching all programs from database");

        // Get all programs from database
        List<Program> programs = repository.findAll();

        logger.info("Found {} programs", programs.size());

        // Convert each Program entity to ProgramResponse
        List<ProgramResponse> responses = new java.util.ArrayList<>();
        for (Program program : programs) {
            ProgramResponse response = convertToResponse(program);
            responses.add(response);
        }

        return responses;
    }

    /**
     * Get a single program by its ID.
     * Throws exception if program not found.
     */
    public ProgramResponse getById(Long id) {
        logger.info("Fetching program with id: {}", id);

        // Find program by ID - throws exception if not found
        Program program = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Program not found with id: {}", id);
                return new RuntimeException("Program not found with id: " + id);
            });

        logger.info("Found program: {}", program.getTitle());

        return convertToResponse(program);
    }

    /**
     * Create a new program with optional image file.
     * Accepts program details and image file, converts image to bytes, and saves to database.
     *
     * @param request Program details (title, description, benefits)
     * @param imageFile Optional image file (MultipartFile)
     * @return Created program response
     */
    public ProgramResponse create(ProgramRequest request, MultipartFile imageFile) throws IOException {
        logger.info("Creating new program: {}", request.getTitle());

        // Create new Program entity
        Program program = new Program();

        // Set title from request
        program.setTitle(request.getTitle());
        logger.info("Title set to: {}", request.getTitle());

        // Set description from request (can be null)
        if (request.getDescription() != null) {
            program.setDescription(request.getDescription());
        }

        // Set benefits from request (can be null)
        if (request.getBenefits() != null) {
            program.setBenefits(request.getBenefits());
        }

        // Handle image file upload if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Processing image file: {}", imageFile.getOriginalFilename());

            // Get original filename
            String originalFilename = imageFile.getOriginalFilename();
            program.setImageName(originalFilename);
            logger.info("Image name stored: {}", originalFilename);

            // Get content type (MIME type) - e.g., "image/jpeg", "image/png"
            String contentType = imageFile.getContentType();
            program.setImageType(contentType);
            logger.info("Image type stored: {}", contentType);

            // Convert image file to byte array
            byte[] imageBytes = imageFile.getBytes();
            program.setImageData(imageBytes);
            logger.info("Image data converted to bytes: {} bytes", imageBytes.length);
        } else {
            logger.info("No image file provided");
        }

        // Save to database
        Program savedProgram = repository.save(program);
        logger.info("Program saved with id: {}", savedProgram.getId());

        // Return converted response
        return convertToResponse(savedProgram);
    }

    /**
     * Update an existing program with optional new image.
     * Finds program by ID, updates its details, and optionally updates image.
     *
     * @param id Program ID
     * @param request Updated program details
     * @param imageFile Optional new image file
     * @return Updated program response
     */
    public ProgramResponse update(Long id, ProgramRequest request, MultipartFile imageFile) throws IOException {
        logger.info("Updating program with id: {}", id);

        // Find existing program - throws exception if not found
        Program program = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Program not found with id: {}", id);
                return new RuntimeException("Program not found with id: " + id);
            });

        // Update title
        if (request.getTitle() != null) {
            program.setTitle(request.getTitle());
            logger.info("Title updated to: {}", request.getTitle());
        }

        // Update description
        if (request.getDescription() != null) {
            program.setDescription(request.getDescription());
        }

        // Update benefits if provided
        if (request.getBenefits() != null) {
            program.setBenefits(request.getBenefits());
        }

        // Handle new image file upload if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            logger.info("Updating image file: {}", imageFile.getOriginalFilename());

            // Update image name
            String originalFilename = imageFile.getOriginalFilename();
            program.setImageName(originalFilename);
            logger.info("Image name updated to: {}", originalFilename);

            // Update image type
            String contentType = imageFile.getContentType();
            program.setImageType(contentType);
            logger.info("Image type updated to: {}", contentType);

            // Convert new image file to byte array
            byte[] imageBytes = imageFile.getBytes();
            program.setImageData(imageBytes);
            logger.info("Image data updated: {} bytes", imageBytes.length);
        } else {
            logger.info("No new image file provided - keeping existing image");
        }

        // Save updated program
        Program updatedProgram = repository.save(program);
        logger.info("Program updated successfully: {}", updatedProgram.getTitle());

        return convertToResponse(updatedProgram);
    }

    /**
     * Delete a program by its ID.
     * Throws exception if program not found.
     *
     * @param id Program ID to delete
     */
    public void delete(Long id) {
        logger.info("Deleting program with id: {}", id);

        // Check if program exists before deleting
        Program program = repository.findById(id)
            .orElseThrow(() -> {
                logger.error("Program not found with id: {}", id);
                return new RuntimeException("Program not found with id: " + id);
            });

        // Delete from database
        repository.delete(program);
        logger.info("Program deleted successfully with id: {}", id);
    }

    /**
     * Convert Program entity to ProgramResponse DTO.
     * Maps all fields from entity to response object.
     * Image bytes will be automatically base64-encoded by Spring when converting to JSON.
     *
     * @param program Program entity to convert
     * @return Program response DTO
     */
    private ProgramResponse convertToResponse(Program program) {
        // Create new response object
        ProgramResponse response = new ProgramResponse();

        // Copy all fields from entity to response
        response.setId(program.getId());
        response.setTitle(program.getTitle());
        response.setDescription(program.getDescription());
        response.setImageName(program.getImageName());
        response.setImageType(program.getImageType());
        response.setImageData(program.getImageData());
        response.setBenefits(program.getBenefits());
        response.setCreatedAt(program.getCreatedAt());
        response.setUpdatedAt(program.getUpdatedAt());

        return response;
    }
}