package com.expensetracker.service;

import com.expensetracker.dto.response.ReportResponse;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    
    public ReportResponse getMonthlyReport(String username, int year, int month) {
        User user = getUserByUsername(username);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<Expense> expenses = expenseRepository.findExpensesByUserAndDateRange(user, startDate, endDate);
        
        return generateReport(expenses, "Monthly Report - " + yearMonth);
    }
    
    public ReportResponse getWeeklyReport(String username, LocalDate startDate) {
        User user = getUserByUsername(username);
        LocalDate endDate = startDate.plusDays(6);
        
        List<Expense> expenses = expenseRepository.findExpensesByUserAndDateRange(user, startDate, endDate);
        
        return generateReport(expenses, "Weekly Report - " + startDate + " to " + endDate);
    }
    
    public ReportResponse getCategoryWiseReport(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        List<Expense> expenses = expenseRepository.findExpensesByUserAndDateRange(user, startDate, endDate);
        
        return generateReport(expenses, "Category-wise Report - " + startDate + " to " + endDate);
    }
    
    public ReportResponse getYearlyReport(String username, int year) {
        User user = getUserByUsername(username);
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        List<Expense> expenses = expenseRepository.findExpensesByUserAndDateRange(user, startDate, endDate);
        
        return generateReport(expenses, "Yearly Report - " + year);
    }
    
    private ReportResponse generateReport(List<Expense> expenses, String period) {
        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long totalExpenses = expenses.size();
        
        Map<String, BigDecimal> categoryWiseExpenses = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        
        return new ReportResponse(totalAmount, totalExpenses, categoryWiseExpenses, period);
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
