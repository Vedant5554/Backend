package com.example.uams.module.invoice.entity;

import com.example.uams.module.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Maps to the 'invoices' table.
 *
 * Relationships:
 *  - ManyToOne → Student
 */
@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "invoice_number", unique = true, length = 50)
    private String invoiceNumber;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "first_reminder_date")
    private LocalDate firstReminderDate;

    @Column(name = "second_reminder_date")
    private LocalDate secondReminderDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    // PENDING, PAID, OVERDUE, CANCELLED
    @Column(name = "status", length = 20)
    private String status;
}