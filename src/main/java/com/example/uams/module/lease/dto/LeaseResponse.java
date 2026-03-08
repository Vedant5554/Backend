package com.example.uams.module.lease.dto;

import com.example.uams.module.lease.entity.LeaseAgreement;
import com.example.uams.module.lease.entity.LeaseStatus;
import com.example.uams.module.lease.entity.Semester;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class LeaseResponse {

    private Long        leaseId;
    private String      leaseNumber;
    private Long        studentId;
    private String      studentName;
    private LocalDate   startDate;
    private LocalDate   endDate;
    private BigDecimal  monthlyRent;
    private BigDecimal  depositAmount;
    private Semester    semester;
    private LeaseStatus status;
    private String      notes;

    public static LeaseResponse from(LeaseAgreement l) {
        LeaseResponse r = new LeaseResponse();
        r.leaseId       = l.getLeaseId();
        r.leaseNumber   = l.getLeaseNumber();
        r.studentId     = l.getStudent().getStudentId();
        r.studentName   = l.getStudent().getFirstName()
                + " " + l.getStudent().getLastName();
        r.startDate     = l.getStartDate();
        r.endDate       = l.getEndDate();
        r.monthlyRent   = l.getMonthlyRent();
        r.depositAmount = l.getDepositAmount();
        r.semester      = l.getSemester();
        r.status        = l.getStatus();
        r.notes         = l.getNotes();
        return r;
    }
}