package com.example.uams.module.hall.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.hall.dto.RoomRequest;
import com.example.uams.module.hall.dto.RoomResponse;
import com.example.uams.module.hall.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for room management.
 * All routes nested under /api/halls/{hallId}/rooms
 *
 * GET    /api/halls/{hallId}/rooms                → list all rooms in hall
 * GET    /api/halls/{hallId}/rooms/available      → available rooms in hall
 * GET    /api/halls/{hallId}/rooms/{roomId}       → get one room
 * POST   /api/halls/{hallId}/rooms                → create room in hall
 * PUT    /api/halls/{hallId}/rooms/{roomId}       → update room
 * DELETE /api/halls/{hallId}/rooms/{roomId}       → delete room
 */
@RestController
@RequestMapping("/api/halls/{hallId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // ── GET all ───────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAll(
            @PathVariable Long hallId) {
        return ResponseEntity.ok(ApiResponse.ok(roomService.getByHall(hallId)));
    }

    // ── GET available ─────────────────────────────────────────────────────────
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAvailable(
            @PathVariable Long hallId) {
        return ResponseEntity.ok(
                ApiResponse.ok(roomService.getAvailableByHall(hallId)));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponse>> getById(
            @PathVariable Long hallId,
            @PathVariable Long roomId) {
        return ResponseEntity.ok(ApiResponse.ok(roomService.getById(roomId)));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponse>> create(
            @PathVariable Long hallId,
            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Room created successfully",
                        roomService.create(hallId, request)));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponse>> update(
            @PathVariable Long hallId,
            @PathVariable Long roomId,
            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Room updated successfully",
                roomService.update(hallId, roomId, request)));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long hallId,
            @PathVariable Long roomId) {
        roomService.delete(roomId);
        return ResponseEntity.ok(
                ApiResponse.ok("Room deleted successfully", null));
    }
}