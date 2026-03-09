package com.example.uams.module.nextofkin.entity;

import com.example.uams.module.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

/**
 * Maps to the 'next_of_kin' table.
 *
 * Relationships:
 *  - ManyToOne → Student (one student can have multiple next of kin records)
 */
@Entity
@Table(name = "next_of_kin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NextOfKin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nok_id")
    private Long nokId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "relationship", length = 100)
    private String relationship;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "postcode", length = 20)
    private String postcode;
}