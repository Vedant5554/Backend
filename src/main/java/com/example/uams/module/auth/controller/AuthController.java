package com.example.uams.module.auth.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.auth.dto.LoginRequest;
import com.example.uams.module.auth.dto.LoginResponse;
import com.example.uams.module.auth.dto.RefreshTokenRequest;
import com.example.uams.security.JwtTokenProvider;
import com.example.uams.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints — all PUBLIC (no JWT required).
 *
 * POST /api/auth/login    → validate credentials, return JWT
 * POST /api/auth/refresh  → validate existing token, return new JWT
 * POST /api/auth/logout   → client-side logout (clears SecurityContext)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider      jwtTokenProvider;

    // ── POST /api/auth/login ──────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        // 1. Let Spring Security verify email + password via CustomUserDetailsService
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Store in SecurityContext for this request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate JWT
        String jwt = jwtTokenProvider.generateToken(authentication);

        // 4. Build response
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        LoginResponse response  = LoginResponse.of(
                jwt,
                principal.getId(),
                principal.getEmail(),
                principal.getRole()
        );

        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    // ── POST /api/auth/refresh ────────────────────────────────────────────────

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        String token = request.getToken();

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid or expired token"));
        }

        // Extract user info directly from the existing token's claims
        Long   userId = jwtTokenProvider.getUserIdFromJwt(token);
        String email  = jwtTokenProvider.getEmailFromJwt(token);
        String role   = jwtTokenProvider.getRoleFromJwt(token);

        // Build a minimal authentication object to generate a new token
        UserPrincipal principal = new UserPrincipal(userId, email, "", role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        String newToken = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(ApiResponse.ok("Token refreshed",
                LoginResponse.of(newToken, userId, email, role)));
    }

    // ── POST /api/auth/logout ─────────────────────────────────────────────────

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // JWT is stateless — actual invalidation happens client-side by
        // deleting the token from storage. We just clear the server context.
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", null));
    }
}