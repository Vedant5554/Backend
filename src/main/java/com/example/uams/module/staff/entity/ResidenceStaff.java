package com.example.uams.module.staff.entity;

import com.example.uams.module.hall.entity.ResidenceHall;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(name = "password")
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "role", length = 100)
    private String role;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "street", length = 200)
    private String street;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "postcode", length = 20)
    private String postcode;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "location", length = 100)
    private String location;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;
}