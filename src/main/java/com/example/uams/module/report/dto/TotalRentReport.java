package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Report (d): Total rent paid by a given student.
 */
@Getter
@AllArgsConstructor
public class TotalRentReport {
    private Long            studentId;
    private BigDecimal      totalPaid;
    private List<PaidInvoice> invoices;

    @Getter
    @AllArgsConstructor
    public static class PaidInvoice {
        private String    invoiceNumber;
        private BigDecimal amount;
        private LocalDate  paidDate;
        private String    description;
    }
}