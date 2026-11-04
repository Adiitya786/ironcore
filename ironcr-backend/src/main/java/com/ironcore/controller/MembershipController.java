package com.ironcore.controller;

import com.ironcore.dto.MembershipRequestDto;
import com.ironcore.dto.MembershipResponseDto;
import com.ironcore.service.MembershipRequestService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MEMBERSHIP WORKFLOW CONTROLLER
 * ===============================
 * Full REST endpoint set for membership join requests.
 *
 * FLOW: Frontend submits form → Controller validates → Service saves (transactional)
 * All operations are either fully completed or fully rolled back.
 *
 * Endpoints:
 * POST   /api/membership-requests           → Submit new request
 * GET    /api/membership-requests           → List all (admin)
 * GET    /api/membership-requests/{id}     → Get single
 * PUT    /api/membership-requests/{id}/status → Update status (admin)
 * DELETE /api/membership-requests/{id}     → Delete (admin)
 */
@RestController
@RequestMapping("/api/membership-requests")
public class MembershipController {

    private static final Logger log = LoggerFactory.getLogger(MembershipController.class);
    private final MembershipRequestService service;

    public MembershipController(MembershipRequestService service) {
        this.service = service;
    }

    /**
     * SUBMIT NEW MEMBERSHIP REQUEST
     * Transaction: if save fails → entire request rolls back (no partial DB write)
     */
    @PostMapping
    public ResponseEntity<MembershipResponseDto> create(
            @Valid @RequestBody MembershipRequestDto dto) {
        log.info("POST /api/membership-requests | name={}", dto.getFullName());
        MembershipResponseDto result = service.saveMembershipRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /** List all requests — admin view */
    @GetMapping
    public ResponseEntity<List<MembershipResponseDto>> listAll() {
        log.info("GET /api/membership-requests");
        return ResponseEntity.ok(service.getAllMembershipRequests());
    }

    /** Get single request by ID */
    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponseDto> getById(@PathVariable Long id) {
        log.info("GET /api/membership-requests/{}", id);
        return ResponseEntity.ok(service.getMembershipRequestById(id));
    }

    /** Update status — admin workflow step */
    @PutMapping("/{id}/status")
    public ResponseEntity<MembershipResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("PUT /api/membership-requests/{}/status | new={}", id, status);
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    /** Delete request — admin cleanup only */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/membership-requests/{}", id);
        service.deleteMembershipRequest(id);
        return ResponseEntity.noContent().build();
    }

}
