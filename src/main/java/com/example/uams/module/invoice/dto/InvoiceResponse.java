package com.example.uams.module.invoice.dto;

import com.example.uams.module.invoice.entity.Invoice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class InvoiceResponse {

    private Long       invoiceId;
    private String     invoiceNumber;
    private Long       studentId;
    private String     studentName;
    private String     description;
    private BigDecimal amount;
    private LocalDate  issueDate;
    private LocalDate  dueDate;
    private LocalDate  paidDate;
    private String     status;
    private String     semester;
    private String     paymentMethod;
    private LocalDate  firstReminderDate;
    private LocalDate  secondReminderDate;

    public static InvoiceResponse from(Invoice i) {
        InvoiceResponse r = new InvoiceResponse();
        r.invoiceId           = i.getInvoiceId();
        r.invoiceNumber       = i.getInvoiceNumber();
        r.studentId           = i.getStudent().getStudentId();
        r.studentName         = i.getStudent().getFirstName()
                + " " + i.getStudent().getLastName();
        r.description         = i.getDescription();
        r.amount              = i.getAmount();
        r.issueDate           = i.getIssueDate();
        r.dueDate             = i.getDueDate();
        r.paidDate            = i.getPaidDate();
        r.status              = i.getStatus();
        r.semester            = i.getSemester();
        r.paymentMethod       = i.getPaymentMethod();
        r.firstReminderDate   = i.getFirstReminderDate();
        r.secondReminderDate  = i.getSecondReminderDate();
        return r;
    }
}