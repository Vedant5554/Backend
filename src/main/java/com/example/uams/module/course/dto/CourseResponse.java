package com.example.uams.module.course.dto;

import com.example.uams.module.course.entity.Course;
import com.example.uams.module.course.entity.StudentCourse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Used for both plain course responses and enrolment responses.
 */
@Getter
@NoArgsConstructor
public class CourseResponse {

    private Long      courseId;
    private String    courseName;
    private String    courseCode;
    private String    department;

    // Populated only when returned as part of a student enrolment
    private Long      enrolmentId;
    private LocalDate enrolledDate;

    // ── Plain course factory ──────────────────────────────────────────────────

    public static CourseResponse from(Course course) {
        CourseResponse r = new CourseResponse();
        r.courseId   = course.getCourseId();
        r.courseName = course.getCourseName();
        r.courseCode = course.getCourseCode();
        r.department = course.getDepartment();
        return r;
    }

    // ── Enrolment factory (includes enrolment metadata) ───────────────────────

    public static CourseResponse from(StudentCourse sc) {
        CourseResponse r = from(sc.getCourse());
        r.enrolmentId  = sc.getId();
        r.enrolledDate = sc.getEnrolledDate();
        return r;
    }
}