package com.ironcore.controller;

import com.ironcore.dto.TrialRequest;
import com.ironcore.dto.TrialResponse;
import com.ironcore.service.TrialService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Trial Booking requests.
 * Handles saving and retrieving trial bookings.
 */
@RestController
@RequestMapping("/api/trials")
public class TrialController {

    private static final Logger logger = LoggerFactory.getLogger(TrialController.class);

    private final TrialService trialService;

    public TrialController(TrialService trialService) {
        this.trialService = trialService;
    }

    /**
     * Create a new trial booking.
     * POST /api/trials
     * Body: JSON with fullName, phone, email, preferredDate, preferredTime, etc.
     */
    @PostMapping
    public ResponseEntity<TrialResponse> createTrial(@Valid @RequestBody TrialRequest request) {
        logger.info("POST /api/trials - Creating trial booking for: {}", request.getFullName());

        try {
            // Validate phone is exactly 10 digits
            String phone = request.getPhone();
            if (phone == null || phone.length() != 10 || !phone.matches("^[0-9]{10}$")) {
                logger.error("Invalid phone: {} (must be exactly 10 digits)", phone);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Could add error message here
            }

            // Save trial
            TrialResponse saved = trialService.saveTrial(request);
            logger.info("Trial booking saved with id: {}", saved.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            logger.error("Error saving trial booking: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get all trial bookings.
     * GET /api/trials
     * For admin to see all requests.
     */
    @GetMapping
    public ResponseEntity<List<TrialResponse>> getAllTrials() {
        logger.info("GET /api/trials - Fetching all trial bookings");

        try {
            List<TrialResponse> trials = trialService.getAllTrials();
            logger.info("Found {} trial bookings", trials.size());

            return ResponseEntity.ok(trials);

        } catch (Exception e) {
            logger.error("Error fetching trials: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get single trial by ID.
     * GET /api/trials/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrialResponse> getTrialById(@PathVariable Long id) {
        logger.info("GET /api/trials/{} - Fetching trial", id);

        try {
            TrialResponse trial = trialService.getTrialById(id);
            return ResponseEntity.ok(trial);

        } catch (Exception e) {
            logger.error("Trial not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}