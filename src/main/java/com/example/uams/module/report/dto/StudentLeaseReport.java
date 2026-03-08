package com.example.uams.module.report.dto;

import com.example.uams.module.lease.entity.LeaseStatus;
import com.example.uams.module.lease.entity.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Report (b): Student names and banner numbers with lease agreement details.
 */
@Getter
@AllArgsConstructor
public class StudentLeaseReport {
    private Long        bannerId;
    private String      fullName;
    private String      leaseNumber;
    private Semester    semester;
    private LocalDate   startDate;
    private LocalDate   endDate;
    private BigDecimal  monthlyRent;
    private LeaseStatus status;
}