package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Report (g): Students with room number and place number in a particular hall.
 */
@Getter
@AllArgsConstructor
public class HallStudentReport {
    private Long      bannerId;
    private String    fullName;
    private String    roomNumber;
    private String    placeNumber;
    private String    hallName;
    private LocalDate startDate;
}