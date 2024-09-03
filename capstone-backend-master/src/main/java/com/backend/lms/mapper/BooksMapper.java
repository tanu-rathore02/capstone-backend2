package com.backend.lms.mapper;

import com.backend.lms.dto.books.BooksInDto;
import com.backend.lms.dto.books.BooksOutDto;
import com.backend.lms.dto.categories.CategoriesDto;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.model.Books;
import com.backend.lms.model.Categories;
import com.backend.lms.repository.CategoriesRepository;

public final class BooksMapper {

    public static BooksOutDto mapToBookOutDTO(Books books, BooksOutDto booksOutDto) {
        booksOutDto.setId(books.getId());
        booksOutDto.setTitle(books.getTitle());
        booksOutDto.setAuthor(books.getAuthor());
        booksOutDto.setAvailability(books.getAvailability());
        booksOutDto.setCategories(CategoriesMapper.mapToCategoriesDto(books.getCategories(), new CategoriesDto()));

        return booksOutDto;
    }

    public static Books mapToBook(BooksInDto booksInDto, Books books, CategoriesRepository categoriesRepository) {

        Categories categories = categoriesRepository.findById(booksInDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", booksInDto.getCategoryId().toString())
        );
        books.setTitle(booksInDto.getTitle());
        books.setAuthor(booksInDto.getAuthor());
        books.setAvailability(booksInDto.getAvailability());
        books.setCategories(categories);

        return books;
    }
}
