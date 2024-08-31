package com.backend.lms.mapper;


import com.backend.lms.dto.categories.CategoriesDto;
import com.backend.lms.model.Categories;

public class CategoriesMapper {

    public static CategoriesDto mapToCategoriesDto(Categories categories, CategoriesDto categoriesDto){
        categoriesDto.setId(categories.getId());
        categoriesDto.setCategoryName(categories.getCategoryName());
        return categoriesDto;
    }

    public static Categories mapToCategories(CategoriesDto categoriesDto, Categories categories){
        categories.setCategoryName(categoriesDto.getCategoryName());
        return categories;
    }

}
