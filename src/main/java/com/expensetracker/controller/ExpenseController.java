package com.expensetracker.controller;

import com.expensetracker.dto.request.ExpenseRequest;
import com.expensetracker.dto.response.ExpenseResponse;
import com.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Expenses", description = "Expense management APIs")
public class ExpenseController {
    
    @Autowired
    ExpenseService expenseService;
    
    @GetMapping
    @Operation(summary = "Get all expenses", description = "Retrieve all expenses for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved expenses")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(Authentication authentication) {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses(authentication.getName());
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get expense by ID", description = "Retrieve a specific expense by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved expense"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id, Authentication authentication) {
        ExpenseResponse expense = expenseService.getExpenseById(id, authentication.getName());
        return ResponseEntity.ok(expense);
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "Get expenses by date range", description = "Retrieve expenses within a specific date range")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved expenses")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @Parameter(description = "Start date (YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(
                authentication.getName(), startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    @PostMapping
    @Operation(summary = "Create new expense", description = "Create a new expense entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request,
                                                        Authentication authentication) {
        ExpenseResponse expense = expenseService.createExpense(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update expense", description = "Update an existing expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id,
                                                        @Valid @RequestBody ExpenseRequest request,
                                                        Authentication authentication) {
        ExpenseResponse expense = expenseService.updateExpense(id, request, authentication.getName());
        return ResponseEntity.ok(expense);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete expense", description = "Delete an expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, Authentication authentication) {
        expenseService.deleteExpense(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
