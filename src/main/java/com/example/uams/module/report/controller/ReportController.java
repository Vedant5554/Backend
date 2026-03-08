package com.example.uams.module.report.controller;

import com.example.uams.common.ApiResponse;
import com.example.uams.module.report.dto.*;
import com.example.uams.module.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * All 14 mandatory reports from the problem statement.
 *
 * (a) GET /api/reports/hall-managers
 * (b) GET /api/reports/students-with-leases
 * (c) GET /api/reports/summer-leases
 * (d) GET /api/reports/total-rent-paid/{studentId}
 * (e) GET /api/reports/unpaid-invoices?byDate=YYYY-MM-DD
 * (f) GET /api/reports/unsatisfactory-inspections
 * (g) GET /api/reports/students-in-hall/{hallId}
 * (h) GET /api/reports/waiting-list
 * (i) GET /api/reports/students-by-category
 * (j) GET /api/reports/students-no-next-of-kin
 * (k) GET /api/reports/adviser-for-student/{studentId}
 * (l) GET /api/reports/rent-statistics
 * (m) GET /api/reports/places-per-hall
 * (n) GET /api/reports/staff-over-sixty
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // ── (a) ───────────────────────────────────────────────────────────────────
    @GetMapping("/hall-managers")
    public ResponseEntity<ApiResponse<List<HallManagerReport>>> hallManagers() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.hallManagersReport()));
    }

    // ── (b) ───────────────────────────────────────────────────────────────────
    @GetMapping("/students-with-leases")
    public ResponseEntity<ApiResponse<List<StudentLeaseReport>>> studentsWithLeases() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.studentsWithLeases()));
    }

    // ── (c) ───────────────────────────────────────────────────────────────────
    @GetMapping("/summer-leases")
    public ResponseEntity<ApiResponse<List<SummerLeaseReport>>> summerLeases() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.summerLeases()));
    }

    // ── (d) ───────────────────────────────────────────────────────────────────
    @GetMapping("/total-rent-paid/{studentId}")
    public ResponseEntity<ApiResponse<TotalRentReport>> totalRentPaid(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.totalRentPaid(studentId)));
    }

    // ── (e) ───────────────────────────────────────────────────────────────────
    @GetMapping("/unpaid-invoices")
    public ResponseEntity<ApiResponse<List<UnpaidInvoiceReport>>> unpaidInvoices(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate byDate) {
        return ResponseEntity.ok(
                ApiResponse.ok(reportService.unpaidInvoicesByDate(byDate)));
    }

    // ── (f) ───────────────────────────────────────────────────────────────────
    @GetMapping("/unsatisfactory-inspections")
    public ResponseEntity<ApiResponse<List<InspectionReport>>> unsatisfactoryInspections() {
        return ResponseEntity.ok(
                ApiResponse.ok(reportService.unsatisfactoryInspections()));
    }

    // ── (g) ───────────────────────────────────────────────────────────────────
    @GetMapping("/students-in-hall/{hallId}")
    public ResponseEntity<ApiResponse<List<HallStudentReport>>> studentsInHall(
            @PathVariable Long hallId) {
        return ResponseEntity.ok(
                ApiResponse.ok(reportService.studentsInHall(hallId)));
    }

    // ── (h) ───────────────────────────────────────────────────────────────────
    @GetMapping("/waiting-list")
    public ResponseEntity<ApiResponse<List<WaitingListReport>>> waitingList() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.waitingList()));
    }

    // ── (i) ───────────────────────────────────────────────────────────────────
    @GetMapping("/students-by-category")
    public ResponseEntity<ApiResponse<StudentCategoryReport>> studentsByCategory() {
        return ResponseEntity.ok(
                ApiResponse.ok(reportService.studentCountByCategory()));
    }

    // ── (j) ───────────────────────────────────────────────────────────────────
    @GetMapping("/students-no-next-of-kin")
    public ResponseEntity<ApiResponse<List<NoNextOfKinReport>>> studentsNoNextOfKin() {
        return ResponseEntity.ok(
                ApiResponse.ok(reportService.studentsWithNoNextOfKin()));
    }

    // ── (k) ───────────────────────────────────────────────────────────────────
    @GetMapping("/adviser-for-student/{studentId}")
    public ResponseEntity<ApiResponse<AdviserReport>> adviserForStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                ApiResponse.ok(reportService.adviserForStudent(studentId)));
    }

    // ── (l) ───────────────────────────────────────────────────────────────────
    @GetMapping("/rent-statistics")
    public ResponseEntity<ApiResponse<RentStatisticsReport>> rentStatistics() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.rentStatistics()));
    }

    // ── (m) ───────────────────────────────────────────────────────────────────
    @GetMapping("/places-per-hall")
    public ResponseEntity<ApiResponse<List<HallPlacesReport>>> placesPerHall() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.totalPlacesPerHall()));
    }

    // ── (n) ───────────────────────────────────────────────────────────────────
    @GetMapping("/staff-over-sixty")
    public ResponseEntity<ApiResponse<List<StaffAgeReport>>> staffOverSixty() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.staffOverSixty()));
    }
}