package com.example.uams.module.apartment.entity;

import com.example.uams.module.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Maps to the 'bedrooms' table.
 * A bedroom belongs to an apartment and is assigned to a student.
 *
 * Relationships:
 *  - ManyToOne → StudentApartment
 *  - ManyToOne → Student
 */
@Entity
@Table(name = "bedrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bedroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bedroom_id")
    private Long bedroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    private StudentApartment apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "bedroom_number", nullable = false, length = 20)
    private String bedroomNumber;

    @Column(name = "monthly_fee", precision = 10, scale = 2)
    private BigDecimal monthlyFee;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_occupied")
    private Boolean isOccupied = false;
}