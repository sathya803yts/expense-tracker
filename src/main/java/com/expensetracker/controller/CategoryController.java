package com.expensetracker.controller;

import com.expensetracker.dto.request.CategoryRequest;
import com.expensetracker.dto.response.CategoryResponse;
import com.expensetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all categories for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(Authentication authentication) {
        List<CategoryResponse> categories = categoryService.getAllCategories(authentication.getName());
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id, Authentication authentication) {
        CategoryResponse category = categoryService.getCategoryById(id, authentication.getName());
        return ResponseEntity.ok(category);
    }
    
    @PostMapping
    @Operation(summary = "Create new category", description = "Create a new expense category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or category already exists")
    })
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request,
                                                          Authentication authentication) {
        CategoryResponse category = categoryService.createCategory(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryRequest request,
                                                          Authentication authentication) {
        CategoryResponse category = categoryService.updateCategory(id, request, authentication.getName());
        return ResponseEntity.ok(category);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, Authentication authentication) {
        categoryService.deleteCategory(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
