package com.example.uams.module.student.repository;

import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.entity.StudentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // ── Used by CustomUserDetailsService ─────────────────────────────────────
    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    // ── Used by reports ───────────────────────────────────────────────────────

    // Report: all active students
    List<Student> findByIsActiveTrue();

    // Report: students on waiting list
    List<Student> findByWaitingListTrue();

    // Report: students per category
    List<Student> findByCategory(StudentCategory category);

    // Report: students with no next of kin
    @Query("SELECT s FROM Student s WHERE s.studentId NOT IN " +
            "(SELECT n.student.studentId FROM NextOfKin n)")
    List<Student> findStudentsWithNoNextOfKin();

    // Report: students with lease details (have at least one lease)
    @Query("SELECT DISTINCT s FROM Student s JOIN LeaseAgreement l ON l.student = s")
    List<Student> findStudentsWithLeases();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.adviser WHERE s.email = :email")
    Optional<Student> findByEmailWithAdviser(String email);

    // Search by name (used in admin search)
    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);

    // Filter by adviser
    List<Student> findByAdviser_AdviserId(Long adviserId);
}