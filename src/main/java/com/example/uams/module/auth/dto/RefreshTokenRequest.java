package com.example.uams.module.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/auth/refresh
 *
 * {
 *   "token": "eyJhbGci..."
 * }
 *
 * Note: For a full production app you would use a separate long-lived
 * refresh token stored in the DB. For this project we re-validate
 * the existing token and issue a fresh one if it is still valid.
 */
@Getter
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "Token is required")
    private String token;
}