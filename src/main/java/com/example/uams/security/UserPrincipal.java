package com.example.uams.security;

import com.example.uams.module.student.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Wraps our Student entity into Spring Security's UserDetails contract.
 * Spring Security works with UserDetails internally — this is the bridge.
 */
@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final Long   id;
    private final String email;       // used as username
    private final String password;
    private final String role;        // e.g. "ROLE_STUDENT", "ROLE_ADMIN"

    // ── Static factory ────────────────────────────────────────────────────────

    public static UserPrincipal create(Student student) {
        return new UserPrincipal(
                student.getStudentId(),
                student.getEmail(),
                student.getPassword(),
                "ROLE_STUDENT"        // expand this when you add staff/admin login
        );
    }

    // ── UserDetails contract ──────────────────────────────────────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;                 // Spring Security calls this "username"
    }

    @Override public boolean isAccountNonExpired()    { return true; }
    @Override public boolean isAccountNonLocked()     { return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled()              { return true; }
}