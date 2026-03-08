package com.example.uams.module.hall.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Maps to the 'residence_halls' table.
 *
 * Relationships:
 *  - OneToMany → Room (a hall has many rooms)
 */
@Entity
@Table(name = "residence_halls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResidenceHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id")
    private Long hallId;

    @Column(name = "hall_name", nullable = false, length = 200)
    private String hallName;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "total_rooms")
    private Integer totalRooms;

    // Manager name — links conceptually to ResidenceStaff
    @Column(name = "manager_name", length = 200)
    private String managerName;

    // Manager phone — used in report: "list halls with manager phone"
    @Column(name = "manager_phone", length = 20)
    private String managerPhone;
}