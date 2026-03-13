package com.example.uams.module.invoice.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.invoice.dto.InvoiceRequest;
import com.example.uams.module.invoice.dto.InvoiceResponse;
import com.example.uams.module.invoice.entity.Invoice;
import com.example.uams.module.invoice.repository.InvoiceRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final StudentRepository studentRepository;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public InvoiceResponse create(InvoiceRequest request) {
        Student student = findStudentOrThrow(request.getStudentId());

        if (request.getInvoiceNumber() != null
                && invoiceRepository.existsByInvoiceNumber(
                request.getInvoiceNumber())) {
            throw new BadRequestException(
                    "Invoice number already exists: " + request.getInvoiceNumber());
        }

        Invoice invoice = Invoice.builder()
                .student(student)
                .invoiceNumber(request.getInvoiceNumber())
                .description(request.getDescription())
                .amount(request.getAmount())
                .issueDate(request.getIssueDate())
                .dueDate(request.getDueDate())
                .paidDate(request.getPaidDate())
                .status(request.getStatus() != null
                        ? request.getStatus() : "PENDING")
                // ── new fields ──
                .semester(request.getSemester())
                .paymentMethod(request.getPaymentMethod())
                .firstReminderDate(request.getFirstReminderDate())
                .secondReminderDate(request.getSecondReminderDate())
                .build();

        return InvoiceResponse.from(invoiceRepository.save(invoice));
    }

    // ── Read all ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAll() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceResponse::from).toList();
    }

    // ── Read one ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public InvoiceResponse getById(Long id) {
        return InvoiceResponse.from(findOrThrow(id));
    }

    // ── Get by student ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByStudent(Long studentId) {
        findStudentOrThrow(studentId);
        return invoiceRepository.findByStudent_StudentId(studentId).stream()
                .map(InvoiceResponse::from).toList();
    }

    // ── Get by status ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByStatus(String status) {
        return invoiceRepository.findByStatus(status).stream()
                .map(InvoiceResponse::from).toList();
    }

    // ── Get overdue ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getOverdue() {
        return invoiceRepository.findOverdueInvoices(LocalDate.now()).stream()
                .map(InvoiceResponse::from).toList();
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public InvoiceResponse update(Long id, InvoiceRequest request) {
        Invoice invoice = findOrThrow(id);
        Student student = findStudentOrThrow(request.getStudentId());

        if (request.getInvoiceNumber() != null
                && !request.getInvoiceNumber().equals(invoice.getInvoiceNumber())
                && invoiceRepository.existsByInvoiceNumber(
                request.getInvoiceNumber())) {
            throw new BadRequestException(
                    "Invoice number already exists: " + request.getInvoiceNumber());
        }

        invoice.setStudent(student);
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setDescription(request.getDescription());
        invoice.setAmount(request.getAmount());
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setPaidDate(request.getPaidDate());
        invoice.setStatus(request.getStatus());
        // ── new fields ──
        invoice.setSemester(request.getSemester());
        invoice.setPaymentMethod(request.getPaymentMethod());
        invoice.setFirstReminderDate(request.getFirstReminderDate());
        invoice.setSecondReminderDate(request.getSecondReminderDate());

        return InvoiceResponse.from(invoiceRepository.save(invoice));
    }

    // ── Mark as paid ──────────────────────────────────────────────────────────

    @Transactional
    public InvoiceResponse markAsPaid(Long id) {
        Invoice invoice = findOrThrow(id);
        invoice.setStatus("PAID");
        invoice.setPaidDate(LocalDate.now());
        return InvoiceResponse.from(invoiceRepository.save(invoice));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        invoiceRepository.delete(findOrThrow(id));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private Invoice findOrThrow(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", id));
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}