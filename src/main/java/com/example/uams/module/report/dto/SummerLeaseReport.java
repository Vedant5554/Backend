package com.example.uams.module.report.dto;

import com.example.uams.module.lease.entity.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Report (c): Lease agreements that include the summer semester.
 */
@Getter
@AllArgsConstructor
public class SummerLeaseReport {
    private String     leaseNumber;
    private Long       bannerId;
    private String     fullName;
    private LocalDate  startDate;
    private LocalDate  endDate;
    private BigDecimal monthlyRent;
    private Semester   semester;
}