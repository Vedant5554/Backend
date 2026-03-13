package com.example.uams.module.student.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.adviser.entity.Adviser;
import com.example.uams.module.adviser.repository.AdviserRepository;
import com.example.uams.module.student.dto.StudentRequest;
import com.example.uams.module.student.dto.StudentResponse;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final AdviserRepository adviserRepository;
    private final PasswordEncoder   passwordEncoder;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already in use: " + request.getEmail());
        }

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateOfBirth(request.getDateOfBirth())
                .category(request.getCategory())
                .waitingList(request.getWaitingList() != null
                        && request.getWaitingList())
                .isActive(true)
                // ── new fields ──
                .bannerNumber(request.getBannerNumber())
                .street(request.getStreet())
                .city(request.getCity())
                .postcode(request.getPostcode())
                .mobilePhone(request.getMobilePhone())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .specialNeeds(request.getSpecialNeeds())
                .additionalComments(request.getAdditionalComments())
                .major(request.getMajor())
                .minor(request.getMinor())
                .build();

        if (request.getAdviserId() != null) {
            Adviser adviser = adviserRepository.findById(request.getAdviserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Adviser", request.getAdviserId()));
            student.setAdviser(adviser);
        }

        return StudentResponse.from(studentRepository.save(student));
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

    // ── Read one ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return StudentResponse.from(findOrThrow(id));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findOrThrow(id);

        // Check email uniqueness only if it changed
        if (!student.getEmail().equals(request.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already in use: " + request.getEmail());
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setCategory(request.getCategory());
        student.setWaitingList(request.getWaitingList() != null
                && request.getWaitingList());

        // ── new fields ───────────────────────────────────────────────────────
        student.setBannerNumber(request.getBannerNumber());
        student.setStreet(request.getStreet());
        student.setCity(request.getCity());
        student.setPostcode(request.getPostcode());
        student.setMobilePhone(request.getMobilePhone());
        student.setGender(request.getGender());
        student.setNationality(request.getNationality());
        student.setSpecialNeeds(request.getSpecialNeeds());
        student.setAdditionalComments(request.getAdditionalComments());
        student.setMajor(request.getMajor());
        student.setMinor(request.getMinor());

        // Only re-hash if a new password was provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            student.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getAdviserId() != null) {
            Adviser adviser = adviserRepository.findById(request.getAdviserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Adviser", request.getAdviserId()));
            student.setAdviser(adviser);
        }

        return StudentResponse.from(studentRepository.save(student));
    }

    // ── Soft delete (deactivate) ──────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        Student student = findOrThrow(id);
        student.setIsActive(false);
        studentRepository.save(student);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StudentResponse> search(String name) {
        return studentRepository.searchByName(name)
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

    // ── Waiting list ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<StudentResponse> getWaitingList() {
        return studentRepository.findByWaitingListTrue()
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    private Student findOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}