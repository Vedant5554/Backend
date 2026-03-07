package com.example.uams.module.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response body for POST /api/auth/login
 *
 * {
 *   "accessToken": "eyJhbGci...",
 *   "tokenType":   "Bearer",
 *   "userId":      1,
 *   "email":       "student@uni.com",
 *   "role":        "ROLE_STUDENT"
 * }
 */
@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private Long   userId;
    private String email;
    private String role;

    // ── Convenience factory ───────────────────────────────────────────────────

    public static LoginResponse of(String token, Long userId, String email, String role) {
        return new LoginResponse(token, "Bearer", userId, email, role);
    }
}