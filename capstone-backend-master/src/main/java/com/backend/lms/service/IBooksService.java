package com.backend.lms.service;

import com.backend.lms.dto.books.BooksInDto;
import com.backend.lms.dto.books.BooksOutDto;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;


@Service
public interface IBooksService {

    List<BooksOutDto> getAllBooks(Sort sort);
    Page<BooksOutDto> getBooksPaginated(Pageable pageable, String search);
    Long getBookTitleCount();

    BooksOutDto getBookByTitle(String title);
    BooksOutDto getBookById(Long id);
    List<BooksOutDto> getBooksByAuthor(String author);
    List<BooksOutDto> getBooksByCategoryId(Long id);

    BooksOutDto createBook(BooksInDto booksInDto);

    BooksOutDto updateBook(Long id, BooksInDto booksInDTO);

    BooksOutDto deleteBookByTitle(String title);
    BooksOutDto deleteBookById(Long id);

}
