package com.example.uams.module.lease.dto;

import com.example.uams.module.lease.entity.LeaseStatus;
import com.example.uams.module.lease.entity.Semester;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request body for POST /api/leases  and  PUT /api/leases/{id}
 */
@Getter
@NoArgsConstructor
public class LeaseRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @Size(max = 50, message = "Lease number must not exceed 50 characters")
    private String leaseNumber;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private BigDecimal monthlyRent;

    private BigDecimal depositAmount;

    private Semester semester;

    private LeaseStatus status = LeaseStatus.ACTIVE;

    private String notes;
}