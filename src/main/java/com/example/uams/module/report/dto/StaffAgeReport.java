package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Report (n): Staff number, name, age, and location of staff over 60 years old.
 */
@Getter
@AllArgsConstructor
public class StaffAgeReport {
    private Long   staffId;
    private String fullName;
    private int    age;
    private String role;
    private String location;
}