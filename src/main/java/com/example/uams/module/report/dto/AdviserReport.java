package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Report (k): Name and internal telephone number of the adviser for a student.
 */
@Getter
@AllArgsConstructor
public class AdviserReport {
    private String adviserName;
    private String phone;
    private String email;
    private String department;
}