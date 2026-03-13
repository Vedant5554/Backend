package com.example.uams.module.course.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/courses  and  PUT /api/courses/{id}
 */
@Getter
@NoArgsConstructor
public class CourseRequest {

    @NotBlank(message = "Course name is required")
    @Size(max = 200, message = "Course name must not exceed 200 characters")
    private String courseName;

    @Size(max = 20, message = "Course code must not exceed 20 characters")
    private String courseCode;

    @Size(max = 200, message = "Department must not exceed 200 characters")
    private String department;

    // ── Instructor fields (spec requirements) ─────────────────────────────────

    @Size(max = 200)
    private String instructorName;

    @Size(max = 20)
    private String instructorPhone;

    @Email(message = "Instructor email must be valid")
    private String instructorEmail;

    @Size(max = 50)
    private String instructorRoomNumber;
}