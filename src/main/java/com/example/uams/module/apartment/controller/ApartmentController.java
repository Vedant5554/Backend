package com.example.uams.module.apartment.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.apartment.dto.*;
import com.example.uams.module.apartment.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for apartment management.
 *
 * Apartments:
 *   GET    /api/apartments                                   → list all
 *   GET    /api/apartments/{id}                              → get one
 *   GET    /api/apartments/search?q=                         → search
 *   POST   /api/apartments                                   → create
 *   PUT    /api/apartments/{id}                              → update
 *   DELETE /api/apartments/{id}                              → delete
 *
 * Bedrooms:
 *   GET    /api/apartments/{id}/bedrooms                     → list all bedrooms
 *   GET    /api/apartments/{id}/bedrooms/available           → available bedrooms
 *   POST   /api/apartments/{id}/bedrooms                     → add bedroom
 *   PUT    /api/apartments/{id}/bedrooms/{bedroomId}         → update bedroom
 *   DELETE /api/apartments/{id}/bedrooms/{bedroomId}         → delete bedroom
 *
 * Inspections:
 *   GET    /api/apartments/{id}/inspections                  → list inspections
 *   POST   /api/apartments/{id}/inspections                  → add inspection
 *   PUT    /api/apartments/{id}/inspections/{inspectionId}   → update inspection
 *   DELETE /api/apartments/{id}/inspections/{inspectionId}   → delete inspection
 */
@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    // ── Apartments ────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApartmentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(apartmentService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApartmentResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(apartmentService.getById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ApartmentResponse>>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(apartmentService.search(q)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApartmentResponse>> create(
            @Valid @RequestBody ApartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Apartment created successfully",
                        apartmentService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ApartmentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ApartmentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Apartment updated successfully",
                apartmentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        apartmentService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Apartment deleted successfully", null));
    }

    // ── Bedrooms ──────────────────────────────────────────────────────────────

    @GetMapping("/{id}/bedrooms")
    public ResponseEntity<ApiResponse<List<BedroomResponse>>> getBedrooms(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(apartmentService.getBedroomsByApartment(id)));
    }

    @GetMapping("/{id}/bedrooms/available")
    public ResponseEntity<ApiResponse<List<BedroomResponse>>> getAvailableBedrooms(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(apartmentService.getAvailableBedrooms(id)));
    }

    @PostMapping("/{id}/bedrooms")
    public ResponseEntity<ApiResponse<BedroomResponse>> createBedroom(
            @PathVariable Long id,
            @Valid @RequestBody BedroomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Bedroom created successfully",
                        apartmentService.createBedroom(id, request)));
    }

    @PutMapping("/{id}/bedrooms/{bedroomId}")
    public ResponseEntity<ApiResponse<BedroomResponse>> updateBedroom(
            @PathVariable Long id,
            @PathVariable Long bedroomId,
            @Valid @RequestBody BedroomRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Bedroom updated successfully",
                apartmentService.updateBedroom(id, bedroomId, request)));
    }

    @DeleteMapping("/{id}/bedrooms/{bedroomId}")
    public ResponseEntity<ApiResponse<Void>> deleteBedroom(
            @PathVariable Long id,
            @PathVariable Long bedroomId) {
        apartmentService.deleteBedroom(bedroomId);
        return ResponseEntity.ok(
                ApiResponse.ok("Bedroom deleted successfully", null));
    }

    // ── Inspections ───────────────────────────────────────────────────────────

    @GetMapping("/{id}/inspections")
    public ResponseEntity<ApiResponse<List<InspectionResponse>>> getInspections(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(apartmentService.getInspectionsByApartment(id)));
    }

    @PostMapping("/{id}/inspections")
    public ResponseEntity<ApiResponse<InspectionResponse>> createInspection(
            @PathVariable Long id,
            @Valid @RequestBody InspectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Inspection created successfully",
                        apartmentService.createInspection(id, request)));
    }

    @PutMapping("/{id}/inspections/{inspectionId}")
    public ResponseEntity<ApiResponse<InspectionResponse>> updateInspection(
            @PathVariable Long id,
            @PathVariable Long inspectionId,
            @Valid @RequestBody InspectionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Inspection updated successfully",
                apartmentService.updateInspection(inspectionId, request)));
    }

    @DeleteMapping("/{id}/inspections/{inspectionId}")
    public ResponseEntity<ApiResponse<Void>> deleteInspection(
            @PathVariable Long id,
            @PathVariable Long inspectionId) {
        apartmentService.deleteInspection(inspectionId);
        return ResponseEntity.ok(
                ApiResponse.ok("Inspection deleted successfully", null));
    }
}