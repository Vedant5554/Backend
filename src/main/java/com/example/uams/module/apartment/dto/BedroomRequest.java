package com.example.uams.module.apartment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request body for POST /api/apartments/{id}/bedrooms
 * and PUT /api/apartments/{id}/bedrooms/{bedroomId}
 */
@Getter
@NoArgsConstructor
public class BedroomRequest {

    @NotBlank(message = "Bedroom number is required")
    @Size(max = 20)
    private String bedroomNumber;

    private BigDecimal monthlyFee;

    // Optional — assign to a student on creation
    private Long      studentId;
    private LocalDate startDate;
    private LocalDate endDate;
}