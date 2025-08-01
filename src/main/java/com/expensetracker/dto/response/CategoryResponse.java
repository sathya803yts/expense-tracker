package com.expensetracker.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String color;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
