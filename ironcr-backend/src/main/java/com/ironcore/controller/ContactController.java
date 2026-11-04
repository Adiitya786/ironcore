package com.ironcore.controller;

import com.ironcore.dto.ContactRequestDto;
import com.ironcore.dto.ContactResponseDto;
import com.ironcore.service.ContactMessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTACT MESSAGE CONTROLLER
 * ===========================
 * REST endpoints for Contact Us form submissions.
 *
 * FLOW: Frontend submits form → Controller validates → Service saves (transactional)
 * All operations are either fully completed or fully rolled back.
 *
 * Endpoints:
 * POST   /api/contact-messages          → Submit new message
 * GET    /api/contact-messages           → List all (admin)
 * GET    /api/contact-messages/{id}      → Get single
 * PUT    /api/contact-messages/{id}/status → Update status (admin)
 * DELETE /api/contact-messages/{id}      → Delete (admin)
 */
@RestController
@RequestMapping("/api/contact-messages")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    private final ContactMessageService service;

    public ContactController(ContactMessageService service) {
        this.service = service;
    }

    /**
     * SUBMIT NEW CONTACT MESSAGE
     * Transaction: if save fails → entire request rolls back (no partial DB write)
     */
    @PostMapping
    public ResponseEntity<ContactResponseDto> create(
            @Valid @RequestBody ContactRequestDto dto) {
        log.info("POST /api/contact-messages | name={}", dto.getName());
        ContactResponseDto result = service.saveContactMessage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /** List all messages — admin view */
    @GetMapping
    public ResponseEntity<List<ContactResponseDto>> listAll() {
        log.info("GET /api/contact-messages");
        return ResponseEntity.ok(service.getAllMessages());
    }

    /** Get single message by ID */
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDto> getById(@PathVariable Long id) {
        log.info("GET /api/contact-messages/{}", id);
        return ResponseEntity.ok(service.getMessageById(id));
    }

    /** Update status — admin workflow step
     * Query param: ?status=READ | RESPONDED | CLOSED
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ContactResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("PUT /api/contact-messages/{}/status | new={}", id, status);
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    /** Delete message — admin cleanup only */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/contact-messages/{}", id);
        service.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}