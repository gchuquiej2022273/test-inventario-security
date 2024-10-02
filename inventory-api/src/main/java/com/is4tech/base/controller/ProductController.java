package com.is4tech.base.controller;

import com.is4tech.base.domain.Product;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.ProductDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.ProductService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private AuditService auditService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody ProductDto productDto) {
        try {
            Product createdProduct = productService.saveProduct(productDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Product created successfully");
            auditService.createAudit(servletRequest, response, "Product", productDto.getName(), null, "Product created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Product added successfully", createdProduct));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding product", e.getMessage()));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("productId") Integer id, @RequestBody ProductDto productDto) {
        try {
            Product updatedProduct = productService.updateProduct(productDto, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Product updated successfully");
            auditService.createAudit(servletRequest, response, "Product", productDto.getName(), null, "Product updated successfully");

            return ResponseEntity.ok(new ApiResponse("Product updated successfully", updatedProduct));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating product", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("productId") Integer id) {
        try {
            productService.deleteProduct(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Product deleted successfully");
            auditService.createAudit(servletRequest, response, "Product", null, id, "Product deleted successfully");
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting product", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getProducts(HttpServletRequest servletRequest) {
        try {
            List<Product> products = productService.getProducts();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched product list");
            return ResponseEntity.ok(new ApiResponse("Fetched product list:", products));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting products", e.getMessage()));
        }
    }

    @GetMapping("/listId/{productId}")
    public ResponseEntity<ApiResponse> getProductId(HttpServletRequest servletRequest, @PathVariable("productId") Integer id) {
        try {
            Product product = productService.getProductId(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Product found with ID: " + id);
            return ResponseEntity.ok(new ApiResponse("Product successfully obtained", product));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting product", e.getMessage()));
        }
    }
}
