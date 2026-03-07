package com.example.uams.module.course.entity;

import com.example.uams.module.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Maps to the 'student_courses' junction table.
 * Represents the enrolment of a student in a course.
 *
 * Composite PK is handled via @IdClass or @EmbeddedId.
 * We use a surrogate approach here with a separate id column
 * for simplicity with JPA queries.
 */
@Entity
@Table(name = "student_courses",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_id", "course_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_date")
    private LocalDate enrolledDate;
}