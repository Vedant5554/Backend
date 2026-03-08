package com.example.uams.module.apartment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request body for POST /api/apartments/{id}/inspections
 * and PUT /api/apartments/{id}/inspections/{inspectionId}
 */
@Getter
@NoArgsConstructor
public class InspectionRequest {

    @NotNull(message = "Inspection date is required")
    private LocalDate inspectionDate;

    @Size(max = 200)
    private String inspectorName;

    private String remarks;

    @Size(max = 50)
    private String status;   // e.g. PASSED, FAILED, PENDING
}