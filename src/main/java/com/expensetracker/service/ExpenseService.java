package com.expensetracker.service;

import com.expensetracker.dto.request.ExpenseRequest;
import com.expensetracker.dto.response.CategoryResponse;
import com.expensetracker.dto.response.ExpenseResponse;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    public List<ExpenseResponse> getAllExpenses(String username) {
        User user = getUserByUsername(username);
        return expenseRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public ExpenseResponse getExpenseById(Long id, String username) {
        User user = getUserByUsername(username);
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return mapToResponse(expense);
    }
    
    public List<ExpenseResponse> getExpensesByDateRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        return expenseRepository.findExpensesByUserAndDateRange(user, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public ExpenseResponse createExpense(ExpenseRequest request, String username) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setUser(user);
        expense.setCategory(category);
        
        Expense savedExpense = expenseRepository.save(expense);
        return mapToResponse(savedExpense);
    }
    
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request, String username) {
        User user = getUserByUsername(username);
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setCategory(category);
        
        Expense updatedExpense = expenseRepository.save(expense);
        return mapToResponse(updatedExpense);
    }
    
    public void deleteExpense(Long id, String username) {
        User user = getUserByUsername(username);
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(expense);
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    private ExpenseResponse mapToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setExpenseDate(expense.getExpenseDate());
        response.setCreatedAt(expense.getCreatedAt());
        response.setUpdatedAt(expense.getUpdatedAt());
        
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(expense.getCategory().getId());
        categoryResponse.setName(expense.getCategory().getName());
        categoryResponse.setDescription(expense.getCategory().getDescription());
        categoryResponse.setColor(expense.getCategory().getColor());
        response.setCategory(categoryResponse);
        
        return response;
    }
}
