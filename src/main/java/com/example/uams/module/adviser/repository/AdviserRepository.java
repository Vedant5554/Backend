package com.example.uams.module.adviser.repository;

import com.example.uams.module.adviser.entity.Adviser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdviserRepository extends JpaRepository<Adviser, Long> {

    Optional<Adviser> findByEmail(String email);

    boolean existsByEmail(String email);

    // Report: adviser details for a specific student
    @Query("SELECT a FROM Adviser a JOIN Student s ON s.adviser = a " +
            "WHERE s.studentId = :studentId")
    Optional<Adviser> findAdviserByStudentId(@Param("studentId") Long studentId);

    // Search by name or department
    @Query("SELECT a FROM Adviser a WHERE " +
            "LOWER(a.firstName)  LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(a.lastName)   LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(a.department) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Adviser> search(@Param("q") String query);

    // Report: manager phone + hall name (used together with ResidenceHall)
    @Query("SELECT a FROM Adviser a WHERE a.department = :department")
    List<Adviser> findByDepartment(@Param("department") String department);
}