package com.is4tech.base.service;

import com.is4tech.base.domain.Category;
import com.is4tech.base.dto.CategoryDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(CategoryDto input) {
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getDescription() == null || input.getDescription().trim().isEmpty()) {
            throw new Exceptions("Description cannot be empty");
        }
        if (categoryRepository.existsByName(input.getName())) {
            throw new Exceptions("The name already exists");
        }

        Category category = new Category();
        category.setName(input.getName());
        category.setDescription(input.getDescription());
        category.setStatus(true);

        return categoryRepository.save(category);
    }

    public Category updateCategory(CategoryDto input, Integer id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Category not found"));

        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getDescription() == null || input.getDescription().trim().isEmpty()) {
            throw new Exceptions("Description cannot be empty");
        }
        if (categoryRepository.existsByName(input.getName())) {
            throw new Exceptions("The name already exists");
        }

        existingCategory.setName(input.getName());
        existingCategory.setDescription(input.getDescription());

        if (input.getStatus() != null) {
            existingCategory.setStatus(input.getStatus());
        }
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Integer id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Category not found"));

        categoryRepository.delete(existingCategory);
    }

    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new Exceptions("No categories found");
        }
        return categories;
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Category not found with id: " + id));
    }
}
