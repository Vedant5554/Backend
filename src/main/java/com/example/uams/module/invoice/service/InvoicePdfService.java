package com.example.uams.module.invoice.service;

import com.example.uams.module.invoice.entity.Invoice;
import com.example.uams.module.student.entity.Student;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

@Service
public class InvoicePdfService {

    public byte[] generateInvoicePdf(Invoice invoice) {
        Objects.requireNonNull(invoice, "Invoice cannot be null");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // ===== HEADER =====
            document.add(new Paragraph("INVOICE").setBold().setFontSize(18));

            // ===== BASIC DETAILS =====
            document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceId()));

            // ===== STUDENT SAFE ACCESS =====
            Student student = invoice.getStudent();
            String studentName = "N/A";

            if (student != null) {
                String first = student.getFirstName() != null ? student.getFirstName() : "";
                String last  = student.getLastName() != null ? student.getLastName() : "";
                studentName = (first + " " + last).trim();
                if (studentName.isBlank()) studentName = "N/A";
            }

            document.add(new Paragraph("Student: " + studentName));

            // ===== AMOUNT =====
            document.add(new Paragraph("Amount: £" + invoice.getAmount()));

            // ===== STATUS =====
            document.add(new Paragraph("Status: " + invoice.getStatus()));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}