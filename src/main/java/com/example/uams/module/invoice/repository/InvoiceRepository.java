package com.example.uams.module.invoice.repository;

import com.example.uams.module.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // All invoices for a student
    List<Invoice> findByStudent_StudentId(Long studentId);

    // Filter by status
    List<Invoice> findByStatus(String status);

    // Filter by student and status
    List<Invoice> findByStudent_StudentIdAndStatus(Long studentId, String status);

    // Check invoice number uniqueness
    boolean existsByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    // Report: overdue invoices (due date passed and not paid)
    @Query("SELECT i FROM Invoice i WHERE i.status = 'PENDING' " +
            "AND i.dueDate < :today")
    List<Invoice> findOverdueInvoices(@Param("today") LocalDate today);

    // Report: total outstanding amount per student
    @Query("SELECT i FROM Invoice i WHERE i.student.studentId = :studentId " +
            "AND i.status = 'PENDING'")
    List<Invoice> findPendingByStudent(@Param("studentId") Long studentId);

    // Report: invoices within a date range
    @Query("SELECT i FROM Invoice i WHERE i.issueDate BETWEEN :from AND :to")
    List<Invoice> findByIssueDateBetween(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.student WHERE i.invoiceId = :id")
    Optional<Invoice> findByIdWithStudent(@Param("id") Long id);
}