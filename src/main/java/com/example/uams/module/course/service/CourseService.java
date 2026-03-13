package com.example.uams.module.course.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.course.dto.CourseRequest;
import com.example.uams.module.course.dto.CourseResponse;
import com.example.uams.module.course.entity.Course;
import com.example.uams.module.course.entity.StudentCourse;
import com.example.uams.module.course.repository.CourseRepository;
import com.example.uams.module.course.repository.StudentCourseRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository        courseRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final StudentRepository       studentRepository;

    // ── Course CRUD ───────────────────────────────────────────────────────────

    @Transactional
    public CourseResponse create(CourseRequest request) {
        if (request.getCourseCode() != null
                && courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new BadRequestException(
                    "Course code already exists: " + request.getCourseCode());
        }

        Course course = Course.builder()
                .courseName(request.getCourseName())
                .courseCode(request.getCourseCode())
                .department(request.getDepartment())
                // ── new fields ──
                .instructorName(request.getInstructorName())
                .instructorPhone(request.getInstructorPhone())
                .instructorEmail(request.getInstructorEmail())
                .instructorRoomNumber(request.getInstructorRoomNumber())
                .build();

        return CourseResponse.from(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        return CourseResponse.from(findCourseOrThrow(id));
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findCourseOrThrow(id);

        if (request.getCourseCode() != null
                && !request.getCourseCode().equals(course.getCourseCode())
                && courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new BadRequestException(
                    "Course code already exists: " + request.getCourseCode());
        }

        course.setCourseName(request.getCourseName());
        course.setCourseCode(request.getCourseCode());
        course.setDepartment(request.getDepartment());
        // ── new fields ──
        course.setInstructorName(request.getInstructorName());
        course.setInstructorPhone(request.getInstructorPhone());
        course.setInstructorEmail(request.getInstructorEmail());
        course.setInstructorRoomNumber(request.getInstructorRoomNumber());

        return CourseResponse.from(courseRepository.save(course));
    }

    @Transactional
    public void delete(Long id) {
        courseRepository.delete(findCourseOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> search(String query) {
        return courseRepository.search(query)
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    // ── Enrolment ─────────────────────────────────────────────────────────────

    @Transactional
    public CourseResponse enrol(Long studentId, Long courseId) {
        if (studentCourseRepository
                .existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
            throw new BadRequestException(
                    "Student " + studentId + " is already enrolled in course " + courseId);
        }

        Student student = findStudentOrThrow(studentId);
        Course  course  = findCourseOrThrow(courseId);

        StudentCourse sc = StudentCourse.builder()
                .student(student)
                .course(course)
                .enrolledDate(LocalDate.now())
                .build();

        return CourseResponse.from(studentCourseRepository.save(sc));
    }

    @Transactional
    public void unenrol(Long studentId, Long courseId) {
        StudentCourse sc = studentCourseRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrolment not found for student " + studentId
                                + " and course " + courseId));

        studentCourseRepository.delete(sc);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByStudent(Long studentId) {
        findStudentOrThrow(studentId);
        return studentCourseRepository.findByStudent_StudentId(studentId)
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}