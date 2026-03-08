package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Report (f): Apartment inspections where property was unsatisfactory.
 */
@Getter
@AllArgsConstructor
public class InspectionReport {
    private String    apartmentName;
    private String    apartmentAddress;
    private String    inspectorName;
    private LocalDate inspectionDate;
    private String    remarks;
    private String    status;
}