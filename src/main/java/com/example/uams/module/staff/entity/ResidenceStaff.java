package com.example.uams.module.staff.entity;

import com.example.uams.module.hall.entity.ResidenceHall;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Maps to the 'residence_staff' table.
 * Represents staff members assigned to residence halls.
 *
 * Relationships:
 *  - ManyToOne → ResidenceHall
 */
@Entity
@Table(name = "residence_staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResidenceStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private ResidenceHall hall;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "role", length = 100)
    private String role;           // e.g. Hall Manager, Cleaner, Security

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth; // used for report (n): staff over 60

    @Column(name = "is_active")
    private Boolean isActive = true;
}