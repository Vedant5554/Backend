package com.example.uams.module.nextofkin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body for:
 *   POST /api/students/{studentId}/next-of-kin
 *   PUT  /api/students/{studentId}/next-of-kin/{id}
 */
@Getter
@NoArgsConstructor
public class NextOfKinRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 200, message = "Full name must not exceed 200 characters")
    private String fullName;

    @Size(max = 100, message = "Relationship must not exceed 100 characters")
    private String relationship;

    @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Email(message = "Email must be valid")
    private String email;
}