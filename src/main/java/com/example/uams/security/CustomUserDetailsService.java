package com.example.uams.security;

import com.example.uams.module.adviser.entity.Adviser;
import com.example.uams.module.adviser.repository.AdviserRepository;
import com.example.uams.module.staff.entity.ResidenceStaff;
import com.example.uams.module.staff.repository.StaffRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Spring Security calls loadUserByUsername() during authentication.
 * Checks Student, ResidenceStaff, and Adviser tables in order.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final StaffRepository   staffRepository;
    private final AdviserRepository adviserRepository;

    // ── Called by Spring Security during login ────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) return UserPrincipal.create(student.get());

        Optional<ResidenceStaff> staff = staffRepository.findByEmail(email);
        if (staff.isPresent()) return UserPrincipal.create(staff.get());

        Optional<Adviser> adviser = adviserRepository.findByEmail(email);
        if (adviser.isPresent()) return UserPrincipal.create(adviser.get());

        throw new UsernameNotFoundException("No user found with email: " + email);
    }

    // ── Called by JwtAuthenticationFilter on every request ───────────────────

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) return UserPrincipal.create(student.get());

        Optional<ResidenceStaff> staff = staffRepository.findById(id);
        if (staff.isPresent()) return UserPrincipal.create(staff.get());

        Optional<Adviser> adviser = adviserRepository.findById(id);
        if (adviser.isPresent()) return UserPrincipal.create(adviser.get());

        throw new UsernameNotFoundException("No user found with id: " + id);
    }
}