package com.example.uams.module.nextofkin.service;

import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.nextofkin.dto.NextOfKinRequest;
import com.example.uams.module.nextofkin.dto.NextOfKinResponse;
import com.example.uams.module.nextofkin.entity.NextOfKin;
import com.example.uams.module.nextofkin.repository.NextOfKinRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NextOfKinService {

    private final NextOfKinRepository nextOfKinRepository;
    private final StudentRepository   studentRepository;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public NextOfKinResponse create(Long studentId, NextOfKinRequest request) {
        Student student = findStudentOrThrow(studentId);

        NextOfKin nok = NextOfKin.builder()
                .student(student)
                .fullName(request.getFullName())
                .relationship(request.getRelationship())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();

        return NextOfKinResponse.from(nextOfKinRepository.save(nok));
    }

    // ── Get all for a student ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<NextOfKinResponse> getAllByStudent(Long studentId) {
        findStudentOrThrow(studentId);  // validate student exists
        return nextOfKinRepository.findByStudent_StudentId(studentId)
                .stream()
                .map(NextOfKinResponse::from)
                .toList();
    }

    // ── Get one ───────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public NextOfKinResponse getById(Long studentId, Long nokId) {
        return NextOfKinResponse.from(findOrThrow(studentId, nokId));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public NextOfKinResponse update(Long studentId, Long nokId,
                                    NextOfKinRequest request) {
        NextOfKin nok = findOrThrow(studentId, nokId);

        nok.setFullName(request.getFullName());
        nok.setRelationship(request.getRelationship());
        nok.setPhone(request.getPhone());
        nok.setEmail(request.getEmail());

        return NextOfKinResponse.from(nextOfKinRepository.save(nok));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long studentId, Long nokId) {
        nextOfKinRepository.delete(findOrThrow(studentId, nokId));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private Student findStudentOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student", studentId));
    }

    private NextOfKin findOrThrow(Long studentId, Long nokId) {
        NextOfKin nok = nextOfKinRepository.findById(nokId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("NextOfKin", nokId));

        // Make sure the next of kin record actually belongs to this student
        if (!nok.getStudent().getStudentId().equals(studentId)) {
            throw new ResourceNotFoundException(
                    "NextOfKin " + nokId + " does not belong to student " + studentId);
        }
        return nok;
    }
}