package com.example.uams.module.course.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Maps to the 'courses' table.
 *
 * Relationships:
 *  - ManyToMany → Student (via student_courses junction table, managed by StudentCourse)
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name", nullable = false, length = 200)
    private String courseName;

    @Column(name = "course_code", unique = true, length = 20)
    private String courseCode;

    @Column(name = "department", length = 200)
    private String department;
}