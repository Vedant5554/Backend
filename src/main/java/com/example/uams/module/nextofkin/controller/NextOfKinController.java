package com.example.uams.module.nextofkin.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.nextofkin.dto.NextOfKinRequest;
import com.example.uams.module.nextofkin.dto.NextOfKinResponse;
import com.example.uams.module.nextofkin.service.NextOfKinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for next of kin management.
 * All routes are nested under /api/students/{studentId}/next-of-kin
 *
 * GET    /api/students/{studentId}/next-of-kin          → list all for student
 * GET    /api/students/{studentId}/next-of-kin/{id}     → get one
 * POST   /api/students/{studentId}/next-of-kin          → add next of kin
 * PUT    /api/students/{studentId}/next-of-kin/{id}     → update
 * DELETE /api/students/{studentId}/next-of-kin/{id}     → delete
 */
@RestController
@RequestMapping("/api/students/{studentId}/next-of-kin")
@RequiredArgsConstructor
public class NextOfKinController {

    private final NextOfKinService nextOfKinService;

    // ── GET all for student ───────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<NextOfKinResponse>>> getAll(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                ApiResponse.ok(nextOfKinService.getAllByStudent(studentId)));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NextOfKinResponse>> getById(
            @PathVariable Long studentId,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(nextOfKinService.getById(studentId, id)));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<NextOfKinResponse>> create(
            @PathVariable Long studentId,
            @Valid @RequestBody NextOfKinRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Next of kin added successfully",
                        nextOfKinService.create(studentId, request)));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NextOfKinResponse>> update(
            @PathVariable Long studentId,
            @PathVariable Long id,
            @Valid @RequestBody NextOfKinRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Next of kin updated successfully",
                        nextOfKinService.update(studentId, id, request)));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long studentId,
            @PathVariable Long id) {
        nextOfKinService.delete(studentId, id);
        return ResponseEntity.ok(
                ApiResponse.ok("Next of kin deleted successfully", null));
    }
}