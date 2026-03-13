package com.example.uams.module.report.dto;

import com.example.uams.module.student.entity.StudentCategory;
import lombok.Getter;

import java.util.Map;

/**
 * Report (i): Total number of students in each category.
 * Uses a Map so any new StudentCategory values are automatically included.
 */
@Getter
public class StudentCategoryReport {

    private final Map<StudentCategory, Long> countsByCategory;

    public StudentCategoryReport(Map<StudentCategory, Long> countsByCategory) {
        this.countsByCategory = countsByCategory;
    }
}