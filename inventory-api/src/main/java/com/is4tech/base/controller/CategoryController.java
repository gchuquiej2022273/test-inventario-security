package com.is4tech.base.controller;

import com.is4tech.base.domain.Category;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.CategoryDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.CategoryService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AuditService auditService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody CategoryDto categoryDto) {
        try {
            Category createdCategory = categoryService.saveCategory(categoryDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Category created successfully");
            auditService.createAudit(servletRequest, response, "Category", categoryDto.getName(), createdCategory.getId(), "Category created successfully"); // Agrega auditoría aquí
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category added successfully", createdCategory));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding category", e.getMessage()));
        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(HttpServletRequest servletRequest,HttpServletResponse response, @PathVariable("categoryId") Integer id, @RequestBody CategoryDto categoryDto) {
        try {
            Category updatedCategory = categoryService.updateCategory(categoryDto, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Category updated successfully");
            auditService.createAudit(servletRequest, response, "Category", categoryDto.getName(), updatedCategory.getId(), "Category updated successfully"); // Agrega auditoría aquí
            return ResponseEntity.ok(new ApiResponse("Category updated successfully", updatedCategory));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating category", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("categoryId") Integer id) {
        try {
            categoryService.deleteCategory(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Category deleted successfully");
            auditService.createAudit(servletRequest, response, "Category", null, id, "Category deleted successfully"); // Agrega auditoría aquí
            return ResponseEntity.ok(new ApiResponse("Category deleted successfully", null));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting category", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getCategories(HttpServletRequest servletRequest) {
        try {
            List<Category> categories = categoryService.getCategories();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched categories list");
            return ResponseEntity.ok(new ApiResponse("Fetched categories list:", categories));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting categories", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting categories", e.getMessage()));
        }
    }

    @GetMapping("/listId/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(HttpServletRequest servletRequest, @PathVariable("categoryId") Integer id) {
        try {
            Category category = categoryService.getCategoryById(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Category found with ID: " + id);
            return ResponseEntity.ok(new ApiResponse("Category successfully obtained", category));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting category", e.getMessage()));
        }
    }
}
