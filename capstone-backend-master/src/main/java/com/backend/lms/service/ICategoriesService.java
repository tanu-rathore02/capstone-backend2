package com.backend.lms.service;


import com.backend.lms.dto.categories.CategoriesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public interface ICategoriesService {

    List<CategoriesDto> getAllCategories(Sort sort);

    Page<CategoriesDto> getCategoriesPaginated(Pageable pageable, String search);
    ;

    Long getCategoriesCount();

    CategoriesDto getCategoryById(Long id);

    CategoriesDto getCategoryByName(String name);

    CategoriesDto createCategory(CategoriesDto categoriesDto);

    CategoriesDto updateCategory(Long id, CategoriesDto categoriesDto);

    CategoriesDto deleteCategoryById(Long id);

    CategoriesDto deleteCategoryByName(String name);
}
