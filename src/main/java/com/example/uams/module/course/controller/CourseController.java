package com.example.uams.module.course.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.course.dto.CourseRequest;
import com.example.uams.module.course.dto.CourseResponse;
import com.example.uams.module.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for course management and student enrolments.
 *
 * GET    /api/courses                                   → list all courses
 * GET    /api/courses/{id}                              → get one course
 * GET    /api/courses/search?q=                         → search courses
 * GET    /api/courses/by-student/{studentId}            → courses for a student
 * POST   /api/courses                                   → create course
 * PUT    /api/courses/{id}                              → update course
 * DELETE /api/courses/{id}                              → delete course
 * POST   /api/courses/{courseId}/enrol/{studentId}      → enrol student
 * DELETE /api/courses/{courseId}/enrol/{studentId}      → unenrol student
 */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // ── GET all ───────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(courseService.getAll()));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.getById(id)));
    }

    // ── GET search ────────────────────────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.search(q)));
    }

    // ── GET courses by student ────────────────────────────────────────────────
    @GetMapping("/by-student/{studentId}")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                ApiResponse.ok(courseService.getCoursesByStudent(studentId)));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> create(
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Course created successfully",
                        courseService.create(request)));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Course updated successfully",
                        courseService.update(id, request)));
    }

    // ── DELETE course ─────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Course deleted successfully", null));
    }

    // ── POST enrol student ────────────────────────────────────────────────────
    @PostMapping("/{courseId}/enrol/{studentId}")
    public ResponseEntity<ApiResponse<CourseResponse>> enrol(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Student enrolled successfully",
                        courseService.enrol(studentId, courseId)));
    }

    // ── DELETE unenrol student ────────────────────────────────────────────────
    @DeleteMapping("/{courseId}/enrol/{studentId}")
    public ResponseEntity<ApiResponse<Void>> unenrol(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        courseService.unenrol(studentId, courseId);
        return ResponseEntity.ok(
                ApiResponse.ok("Student unenrolled successfully", null));
    }
}