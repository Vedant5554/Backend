package com.example.uams.module.adviser.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.adviser.dto.AdviserRequest;
import com.example.uams.module.adviser.dto.AdviserResponse;
import com.example.uams.module.adviser.entity.Adviser;
import com.example.uams.module.adviser.repository.AdviserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdviserService {

    private final AdviserRepository adviserRepository;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public AdviserResponse create(AdviserRequest request) {
        if (adviserRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already in use: " + request.getEmail());
        }

        Adviser adviser = Adviser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .build();

        return AdviserResponse.from(adviserRepository.save(adviser));
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AdviserResponse> getAll() {
        return adviserRepository.findAll()
                .stream()
                .map(AdviserResponse::from)
                .toList();
    }

    // ── Read one ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public AdviserResponse getById(Long id) {
        return AdviserResponse.from(findOrThrow(id));
    }

    // ── Get adviser for a specific student (used in reports) ──────────────────

    @Transactional(readOnly = true)
    public AdviserResponse getByStudentId(Long studentId) {
        return adviserRepository.findAdviserByStudentId(studentId)
                .map(AdviserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No adviser found for student id: " + studentId));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public AdviserResponse update(Long id, AdviserRequest request) {
        Adviser adviser = findOrThrow(id);

        if (!adviser.getEmail().equals(request.getEmail())
                && adviserRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already in use: " + request.getEmail());
        }

        adviser.setFirstName(request.getFirstName());
        adviser.setLastName(request.getLastName());
        adviser.setEmail(request.getEmail());
        adviser.setPhone(request.getPhone());
        adviser.setDepartment(request.getDepartment());

        return AdviserResponse.from(adviserRepository.save(adviser));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        adviserRepository.delete(findOrThrow(id));
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AdviserResponse> search(String query) {
        return adviserRepository.search(query)
                .stream()
                .map(AdviserResponse::from)
                .toList();
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    private Adviser findOrThrow(Long id) {
        return adviserRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Adviser", id));
    }
}