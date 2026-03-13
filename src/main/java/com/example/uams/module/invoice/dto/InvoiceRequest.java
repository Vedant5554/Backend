package com.example.uams.module.invoice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request body for POST /api/invoices  and  PUT /api/invoices/{id}
 */
@Getter
@NoArgsConstructor
public class InvoiceRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @Size(max = 50, message = "Invoice number must not exceed 50 characters")
    private String invoiceNumber;

    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate paidDate;

    // PENDING, PAID, OVERDUE, CANCELLED
    @Size(max = 20)
    private String status = "PENDING";

    // ── Added fields (spec requirements) ─────────────────────────────────────

    @Size(max = 20)
    private String semester;            // e.g. YEAR1_SEM1, SUMMER

    @Size(max = 50)
    private String paymentMethod;       // CASH, VISA, CHECK

    private LocalDate firstReminderDate;

    private LocalDate secondReminderDate;
}