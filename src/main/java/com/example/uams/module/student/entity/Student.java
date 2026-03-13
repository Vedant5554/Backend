package com.example.uams.module.student.entity;

import com.example.uams.module.adviser.entity.Adviser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Maps to the 'students' table in the database.
 *
 * Relationships:
 *  - ManyToOne  → Adviser       (a student has one adviser, an adviser has many students)
 *  - OneToOne   → NextOfKin     (mapped from NextOfKin side)
 *  - OneToMany  → LeaseAgreement, Invoice, HallPlacement, StudentCourse
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    // Stored as BCrypt hash — never plain text
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "banner_number", unique = true, length = 50)
    private String bannerNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private StudentCategory category;

    @Builder.Default
    @Column(name = "waiting_list")
    private Boolean waitingList = false;

    // ── Address ───────────────────────────────────────────────────────────────

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "postcode", length = 20)
    private String postcode;

    // ── Contact & Personal ────────────────────────────────────────────────────

    @Column(name = "mobile_phone", length = 20)
    private String mobilePhone;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "special_needs", columnDefinition = "TEXT")
    private String specialNeeds;

    @Column(name = "additional_comments", columnDefinition = "TEXT")
    private String additionalComments;

    // ── Academic ──────────────────────────────────────────────────────────────

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "minor", length = 100)
    private String minor;

    // ── Status ────────────────────────────────────────────────────────────────

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adviser_id")
    private Adviser adviser;
}