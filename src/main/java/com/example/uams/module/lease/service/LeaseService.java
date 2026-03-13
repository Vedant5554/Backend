package com.example.uams.module.lease.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.lease.dto.LeaseRequest;
import com.example.uams.module.lease.dto.LeaseResponse;
import com.example.uams.module.lease.entity.LeaseAgreement;
import com.example.uams.module.lease.entity.LeaseStatus;
import com.example.uams.module.lease.entity.Semester;
import com.example.uams.module.lease.repository.LeaseRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaseService {

    private final LeaseRepository   leaseRepository;
    private final StudentRepository studentRepository;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public LeaseResponse create(LeaseRequest request) {
        Student student = findStudentOrThrow(request.getStudentId());

        if (request.getLeaseNumber() != null
                && leaseRepository.existsByLeaseNumber(request.getLeaseNumber())) {
            throw new BadRequestException(
                    "Lease number already exists: " + request.getLeaseNumber());
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException(
                    "End date must be after start date");
        }

        LeaseAgreement lease = LeaseAgreement.builder()
                .student(student)
                .leaseNumber(request.getLeaseNumber())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .monthlyRent(request.getMonthlyRent())
                .depositAmount(request.getDepositAmount())
                .semester(request.getSemester())
                .status(request.getStatus() != null
                        ? request.getStatus() : LeaseStatus.ACTIVE)
                .notes(request.getNotes())
                // ── new fields ──
                .enterDate(request.getEnterDate())
                .leaveDate(request.getLeaveDate())
                .placeNumber(request.getPlaceNumber())
                .build();

        return LeaseResponse.from(leaseRepository.save(lease));
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LeaseResponse> getAll() {
        return leaseRepository.findAll().stream()
                .map(LeaseResponse::from).toList();
    }

    // ── Read one ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public LeaseResponse getById(Long id) {
        return LeaseResponse.from(findOrThrow(id));
    }

    // ── Get by student ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LeaseResponse> getByStudent(Long studentId) {
        findStudentOrThrow(studentId);
        return leaseRepository.findByStudent_StudentId(studentId).stream()
                .map(LeaseResponse::from).toList();
    }

    // ── Get by status ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LeaseResponse> getByStatus(LeaseStatus status) {
        return leaseRepository.findByStatus(status).stream()
                .map(LeaseResponse::from).toList();
    }

    // ── Get by semester ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LeaseResponse> getBySemester(Semester semester) {
        return leaseRepository.findBySemester(semester).stream()
                .map(LeaseResponse::from).toList();
    }

    // ── Get expiring soon (within 30 days) ───────────────────────────────────

    @Transactional(readOnly = true)
    public List<LeaseResponse> getExpiringSoon() {
        LocalDate today  = LocalDate.now();
        LocalDate cutoff = today.plusDays(30);
        return leaseRepository.findLeasesExpiringSoon(today, cutoff).stream()
                .map(LeaseResponse::from).toList();
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public LeaseResponse update(Long id, LeaseRequest request) {
        LeaseAgreement lease = findOrThrow(id);
        Student student = findStudentOrThrow(request.getStudentId());

        if (request.getLeaseNumber() != null
                && !request.getLeaseNumber().equals(lease.getLeaseNumber())
                && leaseRepository.existsByLeaseNumber(request.getLeaseNumber())) {
            throw new BadRequestException(
                    "Lease number already exists: " + request.getLeaseNumber());
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        lease.setStudent(student);
        lease.setLeaseNumber(request.getLeaseNumber());
        lease.setStartDate(request.getStartDate());
        lease.setEndDate(request.getEndDate());
        lease.setMonthlyRent(request.getMonthlyRent());
        lease.setDepositAmount(request.getDepositAmount());
        lease.setSemester(request.getSemester());
        lease.setStatus(request.getStatus());
        lease.setNotes(request.getNotes());
        // ── new fields ──
        lease.setEnterDate(request.getEnterDate());
        lease.setLeaveDate(request.getLeaveDate());
        lease.setPlaceNumber(request.getPlaceNumber());

        return LeaseResponse.from(leaseRepository.save(lease));
    }

    // ── Terminate ─────────────────────────────────────────────────────────────

    @Transactional
    public LeaseResponse terminate(Long id) {
        LeaseAgreement lease = findOrThrow(id);
        lease.setStatus(LeaseStatus.TERMINATED);
        return LeaseResponse.from(leaseRepository.save(lease));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        leaseRepository.delete(findOrThrow(id));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private LeaseAgreement findOrThrow(Long id) {
        return leaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "LeaseAgreement", id));
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}