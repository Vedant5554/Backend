package com.example.uams.module.lease.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.lease.dto.LeaseRequest;
import com.example.uams.module.lease.dto.LeaseResponse;
import com.example.uams.module.lease.entity.LeaseStatus;
import com.example.uams.module.lease.entity.Semester;
import com.example.uams.module.lease.service.LeaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for lease management.
 *
 * GET    /api/leases                        → list all leases
 * GET    /api/leases/{id}                   → get one lease
 * GET    /api/leases/student/{studentId}    → leases for a student
 * GET    /api/leases/status/{status}        → filter by status
 * GET    /api/leases/semester/{semester}    → filter by semester
 * GET    /api/leases/expiring-soon          → leases expiring in 30 days
 * POST   /api/leases                        → create lease
 * PUT    /api/leases/{id}                   → update lease
 * PUT    /api/leases/{id}/terminate         → terminate lease
 * DELETE /api/leases/{id}                   → delete lease
 */
@RestController
@RequestMapping("/api/leases")
@RequiredArgsConstructor
public class LeaseController {

    private final LeaseService leaseService;

    // ── GET all ───────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaseResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(leaseService.getAll()));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaseResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(leaseService.getById(id)));
    }

    // ── GET by student ────────────────────────────────────────────────────────
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<LeaseResponse>>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                ApiResponse.ok(leaseService.getByStudent(studentId)));
    }

    // ── GET by status ─────────────────────────────────────────────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<LeaseResponse>>> getByStatus(
            @PathVariable LeaseStatus status) {
        return ResponseEntity.ok(
                ApiResponse.ok(leaseService.getByStatus(status)));
    }

    // ── GET by semester ───────────────────────────────────────────────────────
    @GetMapping("/semester/{semester}")
    public ResponseEntity<ApiResponse<List<LeaseResponse>>> getBySemester(
            @PathVariable Semester semester) {
        return ResponseEntity.ok(
                ApiResponse.ok(leaseService.getBySemester(semester)));
    }

    // ── GET expiring soon ─────────────────────────────────────────────────────
    @GetMapping("/expiring-soon")
    public ResponseEntity<ApiResponse<List<LeaseResponse>>> getExpiringSoon() {
        return ResponseEntity.ok(
                ApiResponse.ok(leaseService.getExpiringSoon()));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<LeaseResponse>> create(
            @Valid @RequestBody LeaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Lease created successfully",
                        leaseService.create(request)));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody LeaseRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Lease updated successfully",
                leaseService.update(id, request)));
    }

    // ── PUT terminate ─────────────────────────────────────────────────────────
    @PutMapping("/{id}/terminate")
    public ResponseEntity<ApiResponse<LeaseResponse>> terminate(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Lease terminated successfully",
                leaseService.terminate(id)));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        leaseService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Lease deleted successfully", null));
    }
}