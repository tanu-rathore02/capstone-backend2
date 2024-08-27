package com.backend.lms.controller;

import com.backend.lms.dto.CategoriesDto;
import com.backend.lms.model.Categories;
import com.backend.lms.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    // Get all categories
    @GetMapping("/allCategories")
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> list = categoriesService.getAllCategories();
        if (list.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(list);
    }

    // Get a category by id
    @GetMapping("/getCategory/{id}")
    public ResponseEntity<CategoriesDto> getCategoryById(@PathVariable int id) {
        CategoriesDto categoryDto = categoriesService.getCategoryById(id);
        if (categoryDto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(categoryDto);
    }

    // Create a new category
    @PostMapping("/createCategory")
    public ResponseEntity<CategoriesDto> createCategory(@RequestBody CategoriesDto categoryDto) {
        try {
            CategoriesDto createdCategory = categoriesService.createCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update a category
    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<CategoriesDto> updateCategory(@PathVariable int id, @RequestBody CategoriesDto categoryDto) {
        try {
            CategoriesDto updatedCategory = categoriesService.updateCategory(id, categoryDto);
            return ResponseEntity.ok().body(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a category
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        try {
            categoriesService.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
