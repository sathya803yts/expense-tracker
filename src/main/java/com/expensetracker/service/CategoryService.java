package com.expensetracker.service;

import com.expensetracker.dto.request.CategoryRequest;
import com.expensetracker.dto.response.CategoryResponse;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    public List<CategoryResponse> getAllCategories(String username) {
        User user = getUserByUsername(username);
        return categoryRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public CategoryResponse getCategoryById(Long id, String username) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToResponse(category);
    }
    
    public CategoryResponse createCategory(CategoryRequest request, String username) {
        User user = getUserByUsername(username);
        
        if (categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new RuntimeException("Category with this name already exists");
        }
        
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        category.setUser(user);
        
        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }
    
    public CategoryResponse updateCategory(Long id, CategoryRequest request, String username) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        
        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }
    
    public void deleteCategory(Long id, String username) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    private CategoryResponse mapToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setColor(category.getColor());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
