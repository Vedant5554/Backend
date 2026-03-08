package com.example.uams.module.staff.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.hall.entity.ResidenceHall;
import com.example.uams.module.hall.repository.ResidenceHallRepository;
import com.example.uams.module.staff.dto.StaffRequest;
import com.example.uams.module.staff.dto.StaffResponse;
import com.example.uams.module.staff.entity.ResidenceStaff;
import com.example.uams.module.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository         staffRepository;
    private final ResidenceHallRepository hallRepository;
    private final PasswordEncoder         passwordEncoder;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public StaffResponse create(StaffRequest request) {
        if (request.getEmail() != null
                && staffRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }

        ResidenceStaff staff = ResidenceStaff.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .dateOfBirth(request.getDateOfBirth())
                .isActive(true)
                .build();

        if (request.getHallId() != null) {
            staff.setHall(findHallOrThrow(request.getHallId()));
        }

        return StaffResponse.from(staffRepository.save(staff));
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StaffResponse> getAll() {
        return staffRepository.findAll().stream()
                .map(StaffResponse::from).toList();
    }

    // ── Read one ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public StaffResponse getById(Long id) {
        return StaffResponse.from(findOrThrow(id));
    }

    // ── Get by hall ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StaffResponse> getByHall(Long hallId) {
        findHallOrThrow(hallId);
        return staffRepository.findByHall_HallId(hallId).stream()
                .map(StaffResponse::from).toList();
    }

    // ── Get by role ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StaffResponse> getByRole(String role) {
        return staffRepository.findByRole(role).stream()
                .map(StaffResponse::from).toList();
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StaffResponse> search(String query) {
        return staffRepository.search(query).stream()
                .map(StaffResponse::from).toList();
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public StaffResponse update(Long id, StaffRequest request) {
        ResidenceStaff staff = findOrThrow(id);

        if (request.getEmail() != null
                && !request.getEmail().equals(staff.getEmail())
                && staffRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }

        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setEmail(request.getEmail());
        staff.setPhone(request.getPhone());
        staff.setRole(request.getRole());
        staff.setDateOfBirth(request.getDateOfBirth());

        // Only re-hash password if a new one is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            staff.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getHallId() != null) {
            staff.setHall(findHallOrThrow(request.getHallId()));
        } else {
            staff.setHall(null);
        }

        return StaffResponse.from(staffRepository.save(staff));
    }

    // ── Soft delete ───────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        ResidenceStaff staff = findOrThrow(id);
        staff.setIsActive(false);
        staffRepository.save(staff);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private ResidenceStaff findOrThrow(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", id));
    }

    private ResidenceHall findHallOrThrow(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", id));
    }
}