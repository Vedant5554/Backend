package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Report (i): Total number of students in each category.
 */
@Getter
@AllArgsConstructor
public class StudentCategoryReport {
    private int undergraduate;
    private int postgraduate;
    private int international;
}