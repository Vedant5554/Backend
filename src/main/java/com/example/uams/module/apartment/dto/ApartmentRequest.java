package com.example.uams.module.apartment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/apartments  and  PUT /api/apartments/{id}
 */
@Getter
@NoArgsConstructor
public class ApartmentRequest {

    @NotBlank(message = "Apartment name is required")
    @Size(max = 200, message = "Apartment name must not exceed 200 characters")
    private String apartmentName;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private Integer totalBedrooms;

    @Size(max = 200)
    private String managerName;

    @Size(max = 20)
    private String managerPhone;
}