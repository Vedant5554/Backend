package com.example.uams.module.hall.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request body for POST /api/halls/{id}/rooms  and  PUT /api/halls/{id}/rooms/{roomId}
 */
@Getter
@NoArgsConstructor
public class RoomRequest {

    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Room number must not exceed 20 characters")
    private String roomNumber;

    @Size(max = 100, message = "Room type must not exceed 100 characters")
    private String roomType;

    private BigDecimal monthlyFee;

    private Boolean isAvailable = true;

    @Size(max = 50, message = "Place number must not exceed 50 characters")
    private String placeNumber;   // unique room identifier used in leases
}