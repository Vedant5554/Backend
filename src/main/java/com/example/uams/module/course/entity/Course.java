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

    @Column(name = "instructor_name", length = 200)
    private String instructorName;

    @Column(name = "instructor_phone", length = 20)
    private String instructorPhone;

    @Column(name = "instructor_email", length = 255)
    private String instructorEmail;

    @Column(name = "instructor_room_number", length = 50)
    private String instructorRoomNumber;
}