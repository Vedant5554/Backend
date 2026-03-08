package com.example.uams.security;

import com.example.uams.module.adviser.entity.Adviser;
import com.example.uams.module.staff.entity.ResidenceStaff;
import com.example.uams.module.student.entity.Student;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security UserDetails wrapper.
 * Supports Student, ResidenceStaff, and Adviser as principals.
 * Constructor is package-visible so AuthController can build a minimal
 * principal for token refresh without hitting the database.
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Long   id;
    private final String email;
    private final String password;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    // Public — used by AuthController refresh endpoint
    public UserPrincipal(Long id, String email, String password, String role) {
        this.id          = id;
        this.email       = email;
        this.password    = password;
        this.role        = role;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // ── Factories ─────────────────────────────────────────────────────────────

    public static UserPrincipal create(Student student) {
        return new UserPrincipal(
                student.getStudentId(),
                student.getEmail(),
                student.getPassword(),
                "STUDENT");
    }

    public static UserPrincipal create(ResidenceStaff staff) {
        return new UserPrincipal(
                staff.getStaffId(),
                staff.getEmail(),
                staff.getPassword(),
                "STAFF");
    }

    public static UserPrincipal create(Adviser adviser) {
        return new UserPrincipal(
                adviser.getAdviserId(),
                adviser.getEmail(),
                adviser.getPassword(),
                "ADVISER");
    }

    // ── UserDetails ───────────────────────────────────────────────────────────

    @Override public String getUsername()                                    { return email; }
    @Override public String getPassword()                                    { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities(){ return authorities; }
    @Override public boolean isAccountNonExpired()                           { return true; }
    @Override public boolean isAccountNonLocked()                            { return true; }
    @Override public boolean isCredentialsNonExpired()                       { return true; }
    @Override public boolean isEnabled()                                     { return true; }
}