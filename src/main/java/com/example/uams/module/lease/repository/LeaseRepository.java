package com.example.uams.module.lease.repository;

import com.example.uams.module.lease.entity.LeaseAgreement;
import com.example.uams.module.lease.entity.LeaseStatus;
import com.example.uams.module.lease.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaseRepository extends JpaRepository<LeaseAgreement, Long> {

    // All leases for a student
    List<LeaseAgreement> findByStudent_StudentId(Long studentId);

    // Active lease for a student
    Optional<LeaseAgreement> findByStudent_StudentIdAndStatus(
            Long studentId, LeaseStatus status);

    // Filter by status
    List<LeaseAgreement> findByStatus(LeaseStatus status);

    // Filter by semester
    List<LeaseAgreement> findBySemester(Semester semester);

    // Check lease number uniqueness
    boolean existsByLeaseNumber(String leaseNumber);

    // Report: leases expiring soon
    @Query("SELECT l FROM LeaseAgreement l WHERE l.status = 'ACTIVE' " +
            "AND l.endDate BETWEEN :today AND :cutoff")
    List<LeaseAgreement> findLeasesExpiringSoon(
            @Param("today") LocalDate today,
            @Param("cutoff") LocalDate cutoff);

    // Report: leases for a specific semester and year
    @Query("SELECT l FROM LeaseAgreement l WHERE l.semester = :semester " +
            "AND YEAR(l.startDate) = :year")
    List<LeaseAgreement> findBySemesterAndYear(
            @Param("semester") Semester semester,
            @Param("year") int year);

    // Report: all active leases with student details
    @Query("SELECT l FROM LeaseAgreement l JOIN FETCH l.student " +
            "WHERE l.status = com.example.uams.module.lease.entity.LeaseStatus.ACTIVE")
    List<LeaseAgreement> findAllActiveWithStudent();
}