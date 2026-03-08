package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Report (e): Students who have not paid their invoices by a given date.
 */
@Getter
@AllArgsConstructor
public class UnpaidInvoiceReport {
    private Long       bannerId;
    private String     fullName;
    private String     invoiceNumber;
    private BigDecimal amount;
    private LocalDate  dueDate;
    private String     status;
}