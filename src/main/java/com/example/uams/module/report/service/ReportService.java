package com.example.uams.module.report.service;

import com.example.uams.module.adviser.repository.AdviserRepository;
import com.example.uams.module.apartment.repository.InspectionRepository;
import com.example.uams.module.course.repository.StudentCourseRepository;
import com.example.uams.module.hall.repository.HallPlacementRepository;
import com.example.uams.module.hall.repository.RoomRepository;
import com.example.uams.module.invoice.repository.InvoiceRepository;
import com.example.uams.module.lease.entity.Semester;
import com.example.uams.module.lease.repository.LeaseRepository;
import com.example.uams.module.report.dto.*;
import com.example.uams.module.staff.repository.StaffRepository;
import com.example.uams.module.student.entity.StudentCategory;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final StudentRepository       studentRepository;
    private final AdviserRepository       adviserRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final HallPlacementRepository placementRepository;
    private final RoomRepository          roomRepository;
    private final InspectionRepository    inspectionRepository;
    private final LeaseRepository         leaseRepository;
    private final InvoiceRepository       invoiceRepository;
    private final StaffRepository         staffRepository;

    // ── (a) Manager name and telephone for each hall of residence ─────────────

    @Transactional(readOnly = true)
    public List<HallManagerReport> hallManagersReport() {
        return staffRepository.findByRole("Hall Manager").stream()
                .map(s -> new HallManagerReport(
                        s.getHall() != null ? s.getHall().getHallName() : "Unassigned",
                        s.getFirstName() + " " + s.getLastName(),
                        s.getPhone()))
                .toList();
    }

    // ── (b) Student names + banner numbers with lease agreement details ────────

    @Transactional(readOnly = true)
    public List<StudentLeaseReport> studentsWithLeases() {
        return leaseRepository.findAllActiveWithStudent().stream()
                .map(l -> new StudentLeaseReport(
                        l.getStudent().getStudentId(),
                        l.getStudent().getFirstName() + " " + l.getStudent().getLastName(),
                        l.getLeaseNumber(),
                        l.getSemester(),
                        l.getStartDate(),
                        l.getEndDate(),
                        l.getMonthlyRent(),
                        l.getStatus()))
                .toList();
    }

    // ── (c) Lease agreements that include the summer semester ─────────────────

    @Transactional(readOnly = true)
    public List<SummerLeaseReport> summerLeases() {
        return leaseRepository.findBySemester(Semester.SUMMER).stream()
                .map(l -> new SummerLeaseReport(
                        l.getLeaseNumber(),
                        l.getStudent().getStudentId(),
                        l.getStudent().getFirstName() + " " + l.getStudent().getLastName(),
                        l.getStartDate(),
                        l.getEndDate(),
                        l.getMonthlyRent(),
                        l.getSemester()))
                .toList();
    }

    // ── (d) Total rent paid by a given student ────────────────────────────────

    @Transactional(readOnly = true)
    public TotalRentReport totalRentPaid(Long studentId) {
        var paidInvoices = invoiceRepository
                .findByStudent_StudentIdAndStatus(studentId, "PAID");

        BigDecimal total = paidInvoices.stream()
                .map(i -> i.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<TotalRentReport.PaidInvoice> invoices = paidInvoices.stream()
                .map(i -> new TotalRentReport.PaidInvoice(
                        i.getInvoiceNumber(),
                        i.getAmount(),
                        i.getPaidDate(),
                        i.getDescription()))
                .toList();

        return new TotalRentReport(studentId, total, invoices);
    }

    // ── (e) Students who have not paid invoices by a given date ───────────────

    @Transactional(readOnly = true)
    public List<UnpaidInvoiceReport> unpaidInvoicesByDate(LocalDate byDate) {
        return invoiceRepository.findOverdueInvoices(byDate).stream()
                .map(i -> new UnpaidInvoiceReport(
                        i.getStudent().getStudentId(),
                        i.getStudent().getFirstName() + " " + i.getStudent().getLastName(),
                        i.getInvoiceNumber(),
                        i.getAmount(),
                        i.getDueDate(),
                        i.getStatus()))
                .toList();
    }

    // ── (f) Apartment inspections where property was unsatisfactory ───────────

    @Transactional(readOnly = true)
    public List<InspectionReport> unsatisfactoryInspections() {
        return inspectionRepository.findByStatus("FAILED").stream()
                .map(i -> new InspectionReport(
                        i.getApartment().getApartmentName(),
                        i.getApartment().getAddress(),
                        i.getInspectorName(),
                        i.getInspectionDate(),
                        i.getRemarks(),
                        i.getStatus()))
                .toList();
    }

    // ── (g) Students with room + place number in a particular hall ────────────

    @Transactional(readOnly = true)
    public List<HallStudentReport> studentsInHall(Long hallId) {
        return placementRepository.findActivePlacementsByHall(hallId).stream()
                .map(p -> new HallStudentReport(
                        p.getStudent().getStudentId(),
                        p.getStudent().getFirstName() + " " + p.getStudent().getLastName(),
                        p.getRoom().getRoomNumber(),
                        p.getRoom().getRoomId(),
                        p.getRoom().getHall().getHallName(),
                        p.getStartDate()))
                .toList();
    }

    // ── (h) Students currently on the waiting list ────────────────────────────

    @Transactional(readOnly = true)
    public List<WaitingListReport> waitingList() {
        return studentRepository.findByWaitingListTrue().stream()
                .map(s -> new WaitingListReport(
                        s.getStudentId(),
                        s.getFirstName() + " " + s.getLastName(),
                        s.getEmail(),
                        s.getCategory()))
                .toList();
    }

    // ── (i) Total number of students in each category ─────────────────────────

    @Transactional(readOnly = true)
    public StudentCategoryReport studentCountByCategory() {
        return new StudentCategoryReport(
                studentRepository.findByCategory(StudentCategory.UNDERGRADUATE).size(),
                studentRepository.findByCategory(StudentCategory.POSTGRADUATE).size(),
                studentRepository.findByCategory(StudentCategory.INTERNATIONAL).size());
    }

    // ── (j) Students who have not supplied next-of-kin details ───────────────

    @Transactional(readOnly = true)
    public List<NoNextOfKinReport> studentsWithNoNextOfKin() {
        return studentRepository.findStudentsWithNoNextOfKin().stream()
                .map(s -> new NoNextOfKinReport(
                        s.getStudentId(),
                        s.getFirstName() + " " + s.getLastName(),
                        s.getEmail(),
                        s.getCategory()))
                .toList();
    }

    // ── (k) Name and internal phone of adviser for a particular student ────────

    @Transactional(readOnly = true)
    public AdviserReport adviserForStudent(Long studentId) {
        return adviserRepository.findAdviserByStudentId(studentId)
                .map(a -> new AdviserReport(
                        a.getFirstName() + " " + a.getLastName(),
                        a.getPhone(),
                        a.getEmail(),
                        a.getDepartment()))
                .orElseThrow(() -> new com.example.uams.exception.ResourceNotFoundException(
                        "Adviser for student", studentId));
    }

    // ── (l) Min, max, average monthly rent for rooms in residence halls ────────

    @Transactional(readOnly = true)
    public RentStatisticsReport rentStatistics() {
        List<BigDecimal> fees = roomRepository.findAll().stream()
                .filter(r -> r.getMonthlyFee() != null)
                .map(r -> r.getMonthlyFee())
                .toList();

        BigDecimal min = fees.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = fees.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal avg = fees.isEmpty() ? BigDecimal.ZERO :
                fees.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(fees.size()), 2, RoundingMode.HALF_UP);

        return new RentStatisticsReport(min, max, avg, fees.size());
    }

    // ── (m) Total number of places in each residence hall ─────────────────────

    @Transactional(readOnly = true)
    public List<HallPlacesReport> totalPlacesPerHall() {
        return roomRepository.findAll().stream()
                .collect(Collectors.groupingBy(r -> r.getHall().getHallId()))
                .entrySet().stream()
                .map(entry -> {
                    var rooms = entry.getValue();
                    var hall  = rooms.get(0).getHall();
                    long available = rooms.stream()
                            .filter(r -> Boolean.TRUE.equals(r.getIsAvailable()))
                            .count();
                    return new HallPlacesReport(
                            hall.getHallId(),
                            hall.getHallName(),
                            rooms.size(),
                            available);
                }).toList();
    }

    // ── (n) Staff over 60 years old — staff number, name, age, location ───────

    @Transactional(readOnly = true)
    public List<StaffAgeReport> staffOverSixty() {
        LocalDate today = LocalDate.now();
        return staffRepository.findByIsActiveTrue().stream()
                .filter(s -> s.getDateOfBirth() != null
                        && Period.between(s.getDateOfBirth(), today).getYears() > 60)
                .map(s -> new StaffAgeReport(
                        s.getStaffId(),
                        s.getFirstName() + " " + s.getLastName(),
                        Period.between(s.getDateOfBirth(), today).getYears(),
                        s.getRole(),
                        s.getHall() != null ? s.getHall().getHallName() : "Residence Office"))
                .toList();
    }
}