package com.example.uams.module.apartment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Maps to the 'apartment_inspections' table.
 * Records inspection events for an apartment.
 *
 * Relationships:
 *  - ManyToOne → StudentApartment
 */
@Entity
@Table(name = "apartment_inspections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_id")
    private Long inspectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    private StudentApartment apartment;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "inspector_name", length = 200)
    private String inspectorName;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    // PASSED, FAILED, PENDING
    @Column(name = "status", length = 50)
    private String status;
}