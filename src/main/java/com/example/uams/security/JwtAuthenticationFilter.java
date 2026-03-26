package com.example.uams.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Runs once per request BEFORE Spring Security's authentication checks.
 *
 * Flow:
 *  1. Read "Authorization: Bearer <token>" header
 *  2. Validate the token via JwtTokenProvider
 *  3. Load the user from DB
 *  4. Set authentication in SecurityContext so the rest of the chain
 *     knows who is making the request
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest  request,
                                    HttpServletResponse response,
                                    FilterChain         filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (!StringUtils.hasText(jwt) || !jwtTokenProvider.validateToken(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = jwtTokenProvider.getUserIdFromJwt(jwt);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            // Don't throw — just let the request continue unauthenticated.
            // SecurityConfig will reject it with 401 if the route is protected.
        }

        filterChain.doFilter(request, response);
    }

    // ── Helper: pull token out of the Authorization header ───────────────────

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);   // strip "Bearer " prefix
        }
        return null;
    }
}