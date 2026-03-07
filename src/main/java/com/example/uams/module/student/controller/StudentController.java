package com.example.uams.module.student.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.student.dto.StudentRequest;
import com.example.uams.module.student.dto.StudentResponse;
import com.example.uams.module.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for student management.
 *
 * GET    /api/students              → list all students
 * GET    /api/students/{id}         → get one student
 * GET    /api/students/search?name= → search by name
 * GET    /api/students/waiting-list → students on waiting list
 * POST   /api/students              → create student
 * PUT    /api/students/{id}         → update student
 * DELETE /api/students/{id}         → soft delete (deactivate)
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ── GET all ───────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getAll()));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getById(id)));
    }

    // ── GET search ────────────────────────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> search(
            @RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.search(name)));
    }

    // ── GET waiting list ──────────────────────────────────────────────────────
    @GetMapping("/waiting-list")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getWaitingList() {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getWaitingList()));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(
            @Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Student created successfully", response));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Student updated successfully",
                        studentService.update(id, request)));
    }

    // ── DELETE (soft) ─────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Student deactivated successfully", null));
    }
}