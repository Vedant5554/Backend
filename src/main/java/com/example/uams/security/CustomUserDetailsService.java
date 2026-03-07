package com.example.uams.security;

import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Security calls loadUserByUsername() during authentication.
 * We look up the student by email and wrap them in UserPrincipal.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    // ── Called by Spring Security during login ────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user found with email: " + email));

        return UserPrincipal.create(student);
    }

    // ── Called by JwtAuthenticationFilter on every request ───────────────────

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user found with id: " + id));

        return UserPrincipal.create(student);
    }
}