package com.example.uams.module.hall.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/halls  and  PUT /api/halls/{id}
 */
@Getter
@NoArgsConstructor
public class HallRequest {

    @NotBlank(message = "Hall name is required")
    @Size(max = 200, message = "Hall name must not exceed 200 characters")
    private String hallName;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private Integer totalRooms;

    @Size(max = 200, message = "Manager name must not exceed 200 characters")
    private String managerName;

    @Size(max = 20, message = "Manager phone must not exceed 20 characters")
    private String managerPhone;
}