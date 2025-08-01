package com.expensetracker.controller;

import com.expensetracker.dto.response.ReportResponse;
import com.expensetracker.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reports", description = "Expense reporting and analytics APIs")
public class ReportController {

    @Autowired
    private ReportService reportService;
    
    @GetMapping("/monthly")
    @Operation(summary = "Get monthly report", description = "Generate monthly expense report with category breakdown")
    @ApiResponse(responseCode = "200", description = "Successfully generated monthly report")
    public ResponseEntity<ReportResponse> getMonthlyReport(
            @Parameter(description = "Year (e.g., 2024)") @RequestParam int year,
            @Parameter(description = "Month (1-12)") @RequestParam int month,
            Authentication authentication) {
        ReportResponse report = reportService.getMonthlyReport(authentication.getName(), year, month);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/weekly")
    @Operation(summary = "Get weekly report", description = "Generate weekly expense report starting from specified date")
    @ApiResponse(responseCode = "200", description = "Successfully generated weekly report")
    public ResponseEntity<ReportResponse> getWeeklyReport(
            @Parameter(description = "Start date of the week (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            Authentication authentication) {
        ReportResponse report = reportService.getWeeklyReport(authentication.getName(), startDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/category-wise")
    @Operation(summary = "Get category-wise report", description = "Generate category-wise expense breakdown for date range")
    @ApiResponse(responseCode = "200", description = "Successfully generated category-wise report")
    public ResponseEntity<ReportResponse> getCategoryWiseReport(
            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        ReportResponse report = reportService.getCategoryWiseReport(authentication.getName(), startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/yearly")
    @Operation(summary = "Get yearly report", description = "Generate yearly expense report with category breakdown")
    @ApiResponse(responseCode = "200", description = "Successfully generated yearly report")
    public ResponseEntity<ReportResponse> getYearlyReport(
            @Parameter(description = "Year (e.g., 2024)") @RequestParam int year,
            Authentication authentication) {
        ReportResponse report = reportService.getYearlyReport(authentication.getName(), year);
        return ResponseEntity.ok(report);
    }
}
