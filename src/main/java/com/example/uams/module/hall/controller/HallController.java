package com.example.uams.module.hall.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.hall.dto.*;
import com.example.uams.module.hall.service.HallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for hall and placement management.
 * Room endpoints are handled by RoomController.
 *
 * GET    /api/halls                          → list all halls
 * GET    /api/halls/{id}                     → get one hall
 * GET    /api/halls/search?q=               → search halls
 * POST   /api/halls                          → create hall
 * PUT    /api/halls/{id}                     → update hall
 * DELETE /api/halls/{id}                     → delete hall
 *
 * GET    /api/halls/placements               → all active placements
 * GET    /api/halls/placements/student/{id}  → placements for a student
 * POST   /api/halls/placements               → create placement
 * PUT    /api/halls/placements/{id}/end      → end a placement
 */
@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    // ── Halls ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<HallResponse>>> getAllHalls() {
        return ResponseEntity.ok(ApiResponse.ok(hallService.getAllHalls()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HallResponse>> getHall(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(hallService.getHallById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HallResponse>>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(hallService.searchHalls(q)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HallResponse>> createHall(
            @Valid @RequestBody HallRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Hall created successfully",
                        hallService.createHall(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HallResponse>> updateHall(
            @PathVariable Long id,
            @Valid @RequestBody HallRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Hall updated successfully",
                hallService.updateHall(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.ok(ApiResponse.ok("Hall deleted successfully", null));
    }

    // ── Placements ────────────────────────────────────────────────────────────

    @GetMapping("/placements")
    public ResponseEntity<ApiResponse<List<PlacementResponse>>> getAllPlacements() {
        return ResponseEntity.ok(
                ApiResponse.ok(hallService.getAllActivePlacements()));
    }

    @GetMapping("/placements/student/{studentId}")
    public ResponseEntity<ApiResponse<List<PlacementResponse>>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                ApiResponse.ok(hallService.getPlacementsByStudent(studentId)));
    }

    @PostMapping("/placements")
    public ResponseEntity<ApiResponse<PlacementResponse>> createPlacement(
            @Valid @RequestBody PlacementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Placement created successfully",
                        hallService.createPlacement(request)));
    }

    @PutMapping("/placements/{id}/end")
    public ResponseEntity<ApiResponse<PlacementResponse>> endPlacement(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Placement ended successfully",
                hallService.endPlacement(id)));
    }
}