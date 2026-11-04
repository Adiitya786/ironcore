package com.ironcore.service;

import com.ironcore.dto.TrialRequest;
import com.ironcore.dto.TrialResponse;
import com.ironcore.entity.Trial;
import com.ironcore.repository.TrialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for Trial booking operations.
 * Simple readable code - no complex streams.
 */
@Service
public class TrialService {

    private static final Logger logger = LoggerFactory.getLogger(TrialService.class);

    private final TrialRepository repository;

    public TrialService(TrialRepository repository) {
        this.repository = repository;
        logger.info("TrialService initialized");
    }

    /**
     * Save a new trial booking.
     * Validates all fields and saves to database.
     */
    public TrialResponse saveTrial(TrialRequest request) {
        logger.info("Saving trial booking for: {}", request.getFullName());

        // Create new Trial entity
        Trial trial = new Trial();

        // Set all fields from request
        trial.setFullName(request.getFullName());
        trial.setPhone(request.getPhone());
        trial.setEmail(request.getEmail());
        trial.setPreferredDate(request.getPreferredDate());
        trial.setPreferredTime(request.getPreferredTime());
        trial.setFitnessGoal(request.getFitnessGoal());
        trial.setExperience(request.getExperience());
        trial.setMessage(request.getMessage());

        // Save to database
        Trial saved = repository.save(trial);
        logger.info("Trial saved with id: {}", saved.getId());

        // Convert to response
        return toResponse(saved);
    }

    /**
     * Get all trial bookings.
     * Returns list for admin to view all requests.
     */
    public List<TrialResponse> getAllTrials() {
        logger.info("Fetching all trial bookings");

        // Get all from database
        List<Trial> trials = repository.findAll();
        logger.info("Found {} trial bookings", trials.size());

        // Convert each to response manually (clear and readable)
        List<TrialResponse> responses = new ArrayList<>();
        for (int i = 0; i < trials.size(); i++) {
            Trial trial = trials.get(i);
            TrialResponse response = toResponse(trial);
            responses.add(response);
        }

        return responses;
    }

    /**
     * Get single trial by ID.
     */
    public TrialResponse getTrialById(Long id) {
        logger.info("Fetching trial with id: {}", id);

        Trial trial = repository.findById(id).orElseThrow(() -> {
            logger.error("Trial not found with id: {}", id);
            return new RuntimeException("Trial booking not found");
        });

        return toResponse(trial);
    }

    /**
     * Convert Trial entity to TrialResponse DTO.
     * Simple mapping - easy to understand.
     */
    private TrialResponse toResponse(Trial trial) {
        TrialResponse res = new TrialResponse();

        res.setId(trial.getId());
        res.setFullName(trial.getFullName());
        res.setPhone(trial.getPhone());
        res.setEmail(trial.getEmail());
        res.setPreferredDate(trial.getPreferredDate());
        res.setPreferredTime(trial.getPreferredTime());
        res.setFitnessGoal(trial.getFitnessGoal());
        res.setExperience(trial.getExperience());
        res.setMessage(trial.getMessage());
        res.setCreatedAt(trial.getCreatedAt());

        return res;
    }
}