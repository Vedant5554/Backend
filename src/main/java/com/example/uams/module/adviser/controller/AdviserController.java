package com.example.uams.module.adviser.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.adviser.dto.AdviserRequest;
import com.example.uams.module.adviser.dto.AdviserResponse;
import com.example.uams.module.adviser.service.AdviserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for adviser management.
 *
 * GET    /api/advisers                        → list all advisers
 * GET    /api/advisers/{id}                   → get one adviser
 * GET    /api/advisers/search?q=              → search by name or department
 * GET    /api/advisers/by-student/{studentId} → get adviser for a student
 * POST   /api/advisers                        → create adviser
 * PUT    /api/advisers/{id}                   → update adviser
 * DELETE /api/advisers/{id}                   → hard delete adviser
 */
@RestController
@RequestMapping("/api/advisers")
@RequiredArgsConstructor
public class AdviserController {

    private final AdviserService adviserService;

    // ── GET all ───────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdviserResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(adviserService.getAll()));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdviserResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(adviserService.getById(id)));
    }

    // ── GET by student ────────────────────────────────────────────────────────
    @GetMapping("/by-student/{studentId}")
    public ResponseEntity<ApiResponse<AdviserResponse>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                ApiResponse.ok(adviserService.getByStudentId(studentId)));
    }

    // ── GET search ────────────────────────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AdviserResponse>>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(adviserService.search(q)));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<AdviserResponse>> create(
            @Valid @RequestBody AdviserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Adviser created successfully",
                        adviserService.create(request)));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdviserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AdviserRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Adviser updated successfully",
                        adviserService.update(id, request)));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adviserService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Adviser deleted successfully", null));
    }
}