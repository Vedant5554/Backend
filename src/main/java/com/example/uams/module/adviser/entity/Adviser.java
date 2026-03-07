package com.example.uams.module.adviser.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Maps to the 'advisers' table.
 *
 * Relationships:
 *  - OneToMany → Student (an adviser can have many students)
 */
@Entity
@Table(name = "advisers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adviser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adviser_id")
    private Long adviserId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "department", length = 200)
    private String department;
}