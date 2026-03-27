package com.example.uams.module.hall.entity;

import com.example.uams.module.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Maps to the 'hall_placements' table.
 * Records which student is placed in which room and when.
 *
 * Relationships:
 *  - ManyToOne → Student
 *  - ManyToOne → Room
 */
@Entity
@Table(name = "hall_placements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallPlacement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placement_id")
    private Long placementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}