package com.expensetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class ReportResponse {
    private BigDecimal totalAmount;
    private Long totalExpenses;
    private Map<String, BigDecimal> categoryWiseExpenses;
    private String period;
}
