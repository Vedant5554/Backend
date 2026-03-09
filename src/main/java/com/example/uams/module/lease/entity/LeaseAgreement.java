package com.example.uams.module.lease.entity;

import com.example.uams.module.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Maps to the 'lease_agreements' table.
 *
 * Relationships:
 *  - ManyToOne → Student
 */
@Entity
@Table(name = "lease_agreements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaseAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lease_id")
    private Long leaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "lease_number", unique = true, length = 50)
    private String leaseNumber;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "enter_date")
    private LocalDate enterDate;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    @Column(name = "place_number", length = 50)
    private String placeNumber;

    @Column(name = "monthly_rent", precision = 10, scale = 2)
    private BigDecimal monthlyRent;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester", length = 10)
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private LeaseStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}