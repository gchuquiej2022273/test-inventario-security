package com.is4tech.base.controller;

import com.is4tech.base.domain.Category;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.CategoryDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.CategoryService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuditService auditService;
    private static final String ENTIDAD = "Category";

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody CategoryDto categoryDto) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, categoryDto.getName(), "Category created successfully");
            Category createdCategory = categoryService.saveCategory(categoryDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Category created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Category added successfully", createdCategory));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, categoryDto.getName(), "Error creating category");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding category", e.getMessage()));
        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(HttpServletRequest servletRequest,HttpServletResponse response, @PathVariable("categoryId") Integer id, @RequestBody CategoryDto categoryDto) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, categoryDto.getName(), "Category updated successfully");
            Category updatedCategory = categoryService.updateCategory(categoryDto, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Category updated successfully");
            return ResponseEntity.ok(new ApiResponse("The category was updated successfully", updatedCategory));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating category", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("categoryId") Integer id) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, null,"Category deleted successfully");
            categoryService.deleteCategory(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Category deleted successfully");
            return ResponseEntity.ok(new ApiResponse("The category was delete successfully", null));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, null,"Error deleting categiry");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting category", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getCategories(HttpServletRequest servletRequest, HttpServletResponse response) {
        try {
            List<Category> categories = categoryService.getCategories();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched categories list");
            return ResponseEntity.ok(new ApiResponse("Fetched categories list:", categories));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, null,"Error listing category");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting categories", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting categories", e.getMessage()));
        }
    }

    @GetMapping("/listId/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(HttpServletResponse response,HttpServletRequest servletRequest, @PathVariable("categoryId") Integer id) {
        try {
            Category category = categoryService.getCategoryById(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Category found with ID: " + id);
            return ResponseEntity.ok(new ApiResponse("Category successfully obtained", category));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, null,"Error listing by id category");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting category", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting category", e.getMessage()));
        }
    }
}
