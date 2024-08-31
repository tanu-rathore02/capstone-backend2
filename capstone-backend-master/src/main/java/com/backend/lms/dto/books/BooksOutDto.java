package com.backend.lms.dto.books;

import lombok.Getter;
import lombok.Setter;
import com.backend.lms.dto.categories.CategoriesDto;
import com.backend.lms.model.Categories;


@Getter
@Setter
public class BooksOutDto {
    private Long id;
    private String title;
    private String author;
    private int availability;
    private CategoriesDto categories;

}
