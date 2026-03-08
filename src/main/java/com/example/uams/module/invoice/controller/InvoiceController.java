package com.example.uams.module.invoice.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.invoice.dto.InvoiceRequest;
import com.example.uams.module.invoice.dto.InvoiceResponse;
import com.example.uams.module.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for invoice management.
 *
 * GET    /api/invoices                        → list all invoices
 * GET    /api/invoices/{id}                   → get one invoice
 * GET    /api/invoices/student/{studentId}    → invoices for a student
 * GET    /api/invoices/status/{status}        → filter by status
 * GET    /api/invoices/overdue                → all overdue invoices
 * POST   /api/invoices                        → create invoice
 * PUT    /api/invoices/{id}                   → update invoice
 * PUT    /api/invoices/{id}/pay               → mark as paid
 * DELETE /api/invoices/{id}                   → delete invoice
 */
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // ── GET all ───────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(invoiceService.getAll()));
    }

    // ── GET one ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(invoiceService.getById(id)));
    }

    // ── GET by student ────────────────────────────────────────────────────────
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                ApiResponse.ok(invoiceService.getByStudent(studentId)));
    }

    // ── GET by status ─────────────────────────────────────────────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(
                ApiResponse.ok(invoiceService.getByStatus(status)));
    }

    // ── GET overdue ───────────────────────────────────────────────────────────
    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getOverdue() {
        return ResponseEntity.ok(ApiResponse.ok(invoiceService.getOverdue()));
    }

    // ── POST create ───────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> create(
            @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Invoice created successfully",
                        invoiceService.create(request)));
    }

    // ── PUT update ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Invoice updated successfully",
                invoiceService.update(id, request)));
    }

    // ── PUT mark as paid ──────────────────────────────────────────────────────
    @PutMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<InvoiceResponse>> markAsPaid(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Invoice marked as paid",
                invoiceService.markAsPaid(id)));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        invoiceService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Invoice deleted successfully", null));
    }
}