package com.example.uams.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Handles all JWT operations:
 *  - generateToken()    → creates a signed JWT from an authenticated user
 *  - getUserIdFromJwt() → extracts the subject (user id) from a token
 *  - getEmailFromJwt()  → extracts the email claim
 *  - getRoleFromJwt()   → extracts the role claim
 *  - validateToken()    → checks signature + expiry, returns true/false
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // ── Generate ──────────────────────────────────────────────────────────────

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userPrincipal.getId()))
                .claim("email", userPrincipal.getEmail())
                .claim("role",  userPrincipal.getRole())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    // ── Extract claims ────────────────────────────────────────────────────────

    public Long getUserIdFromJwt(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public String getEmailFromJwt(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String getRoleFromJwt(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ── Validate ──────────────────────────────────────────────────────────────

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}