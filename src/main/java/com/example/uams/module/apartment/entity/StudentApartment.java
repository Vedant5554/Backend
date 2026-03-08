package com.example.uams.module.apartment.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Maps to the 'student_apartments' table.
 *
 * Relationships:
 *  - OneToMany → Bedroom
 *  - OneToMany → ApartmentInspection
 */
@Entity
@Table(name = "student_apartments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentApartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private Long apartmentId;

    @Column(name = "apartment_name", nullable = false, length = 200)
    private String apartmentName;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "total_bedrooms")
    private Integer totalBedrooms;

    @Column(name = "manager_name", length = 200)
    private String managerName;

    @Column(name = "manager_phone", length = 20)
    private String managerPhone;
}