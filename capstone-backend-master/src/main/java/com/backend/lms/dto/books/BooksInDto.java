package com.backend.lms.dto.books;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BooksInDto {

    @NotEmpty(message = "Title can not be null")
    private String title;
    private String author;
    private int availability;

    @NotEmpty(message = "Category can not be null")
    private Long categoryId;

}
