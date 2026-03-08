package com.example.uams.module.staff.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.staff.dto.StaffRequest;
import com.example.uams.module.staff.dto.StaffResponse;
import com.example.uams.module.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for residence staff management.
 *
 * GET    /api/staff                      → list all staff
 * GET    /api/staff/{id}                 → get one staff member
 * GET    /api/staff/hall/{hallId}        → staff in a specific hall
 * GET    /api/staff/role/{role}          → staff by role
 * GET    /api/staff/search?q=            → search by name
 * POST   /api/staff                      → create staff
 * PUT    /api/staff/{id}                 → update staff
 * DELETE /api/staff/{id}                 → soft delete (deactivate)
 */
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // ── GET all ───────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(staffService.getAll()));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(staffService.getById(id)));
    }

    // ── GET by hall ───────────────────────────────────────────────────────────
    @GetMapping("/hall/{hallId}")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getByHall(
            @PathVariable Long hallId) {
        return ResponseEntity.ok(ApiResponse.ok(staffService.getByHall(hallId)));
    }

    // ── GET by role ───────────────────────────────────────────────────────────
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getByRole(
            @PathVariable String role) {
        return ResponseEntity.ok(ApiResponse.ok(staffService.getByRole(role)));
    }

    // ── GET search ────────────────────────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(staffService.search(q)));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> create(
            @Valid @RequestBody StaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Staff created successfully",
                        staffService.create(request)));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StaffRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Staff updated successfully",
                staffService.update(id, request)));
    }

    // ── DELETE (soft) ─────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Staff deactivated successfully", null));
    }
}