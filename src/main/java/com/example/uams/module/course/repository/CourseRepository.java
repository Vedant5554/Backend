package com.example.uams.module.course.repository;

import com.example.uams.module.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCourseCode(String courseCode);

    // Search by name or code
    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Course> search(@Param("q") String query);

    // All courses in a department
    List<Course> findByDepartment(String department);

    // Report: all courses a specific student is enrolled in
    @Query("SELECT sc.course FROM StudentCourse sc WHERE sc.student.studentId = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);
}