package com.example.uams.module.course.repository;

import com.example.uams.module.course.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    // All enrolments for a student
    List<StudentCourse> findByStudent_StudentId(Long studentId);

    // All enrolments for a course
    List<StudentCourse> findByCourse_CourseId(Long courseId);

    // Check if a student is already enrolled in a course
    boolean existsByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);

    // Find specific enrolment record
    Optional<StudentCourse> findByStudent_StudentIdAndCourse_CourseId(
            Long studentId, Long courseId);

    // Delete all enrolments for a student
    void deleteByStudent_StudentId(Long studentId);
}