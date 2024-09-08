package com.backend.lms.dto.categories;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoriesDto {
    private Long id;

    @NotEmpty(message = "Category name cannot be empty")
    private String categoryName;

}
