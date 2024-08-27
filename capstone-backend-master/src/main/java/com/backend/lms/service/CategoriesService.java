package com.backend.lms.service;

import com.backend.lms.dto.CategoriesDto;
import com.backend.lms.model.Categories;
import com.backend.lms.repository.CategoriesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesService {

    @Autowired
    public CategoriesRepository categoriesRepository;

    // DTO mapping - method to map entity to DTO
    private CategoriesDto mapToDto(Categories categories) {
        CategoriesDto categoriesDto = new CategoriesDto();
        categoriesDto.setId(categories.getId());
        categoriesDto.setCategoryName(categories.getCategoryName());

        return categoriesDto;
    }

    // Entity mapping - method to map DTO to entity
    private Categories mapToEntity(CategoriesDto categoriesDto) {
        Categories categories = new Categories();
        categories.setId(categoriesDto.getId());
        categories.setCategoryName(categoriesDto.getCategoryName());

        return categories;
    }

    // Get all categories
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    // Get a category by id
    public CategoriesDto getCategoryById(int id) {
        Categories categories = categoriesRepository.findById(id).orElse(null);
        if (categories != null) {
            return mapToDto(categories);
        } else {
            return null;
        }
    }

    // Create a new category
    public CategoriesDto createCategory(CategoriesDto categoriesDto) {
        Categories categories = mapToEntity(categoriesDto);
        Categories savedCategory = categoriesRepository.save(categories);
        return mapToDto(savedCategory);
    }

    // Update a category
    public CategoriesDto updateCategory(int id, CategoriesDto categoriesDto) {
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        categories.setCategoryName(categoriesDto.getCategoryName());


        Categories updatedCategory = categoriesRepository.save(categories);
        return mapToDto(updatedCategory);
    }

    // Delete a category
    public void deleteCategory(int id) {
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        categoriesRepository.delete(categories);
    }
}
