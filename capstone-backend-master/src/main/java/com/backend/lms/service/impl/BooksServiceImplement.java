
package com.backend.lms.service.impl;

import com.backend.lms.dto.books.BooksInDto;
import com.backend.lms.dto.books.BooksOutDto;
import com.backend.lms.exception.EntityConstraintViolationException;
import com.backend.lms.exception.MethodNotAllowedException;
import com.backend.lms.exception.ResourceAlreadyExistsException;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.BooksMapper;
import com.backend.lms.model.Books;
import com.backend.lms.model.Categories;
import com.backend.lms.repository.BooksRepository;
import com.backend.lms.repository.CategoriesRepository;
import com.backend.lms.repository.IssuancesRepository;
import com.backend.lms.service.IBooksService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksServiceImplement implements IBooksService {

    private final BooksRepository booksRepository;
    private final CategoriesRepository categoriesRepository;
    private final IssuancesRepository issuancesRepository;


    //get methods
    @Override
    public List<BooksOutDto> getAllBooks(Sort sort) {
        return booksRepository.findAll(sort).stream()
                .map(book -> BooksMapper.mapToBookOutDTO(book, new BooksOutDto()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<BooksOutDto> getBooksPaginated(Pageable pageable, String search) {
        Page<Books> bookPage;
        if (search != null && !search.isEmpty()) {
            bookPage = booksRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            bookPage = booksRepository.findAll(pageable);
        }

        return  bookPage.map(book -> BooksMapper.mapToBookOutDTO(book, new BooksOutDto()));
    }

    @Override
    public Long getBookTitleCount() {
        return booksRepository.count();
    }



    @Override
    public List<BooksOutDto> getBooksByAuthor(String author) {
        List<Books> bookList = booksRepository.findAllByAuthor(author);
        List<BooksOutDto> bookOutDTOList = new ArrayList<>();
        bookList.forEach(book -> bookOutDTOList.add(BooksMapper.mapToBookOutDTO(book, new BooksOutDto())));

        return bookOutDTOList;
    }

    @Override
    public List<BooksOutDto> getBooksByCategoryId(Long id) {
        Categories category = categoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );

        List<Books> bookList = booksRepository.findAllByCategories(category);

        List<BooksOutDto> bookOutDTOList = new ArrayList<>();
        bookList.forEach(book -> bookOutDTOList.add(BooksMapper.mapToBookOutDTO(book, new BooksOutDto())));

        return bookOutDTOList;
    }

    @Override
    public BooksOutDto getBookByTitle(String title) {

        Books book = booksRepository.findByTitleIgnoreCase(title).orElseThrow(
                () -> new ResourceNotFoundException("Book", "title", title)
        );

        BooksOutDto booksOutDto = BooksMapper.mapToBookOutDTO(book, new BooksOutDto());
        return booksOutDto;

    }

    @Override
    public BooksOutDto getBookById(Long id) {

        Books book = booksRepository.findById((long) Math.toIntExact(id)).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", id.toString())
        );

        BooksOutDto booksOutDto = BooksMapper.mapToBookOutDTO(book, new BooksOutDto());
        return booksOutDto;
    }

    //Post method
    @Override
    public BooksOutDto createBook(BooksInDto booksInDto) {

        if (booksRepository.findByTitleIgnoreCase(booksInDto.getTitle()).isPresent()) {
            throw new ResourceAlreadyExistsException("Book", "title", booksInDto.getTitle());
        }

        Books book = BooksMapper.mapToBook(booksInDto, new Books(), categoriesRepository);
        Books savedBook = booksRepository.save(book);

        BooksOutDto booksOutDto = BooksMapper.mapToBookOutDTO(savedBook, new BooksOutDto());

        return booksOutDto;
    }

    //update method
    @Override
    public BooksOutDto updateBook(Long id, BooksInDto booksInDto) {

        Books book = booksRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", id.toString())
        );

        Optional<Books> existingBook = booksRepository.findByTitleIgnoreCase(booksInDto.getTitle());
        if (existingBook.isPresent() && !existingBook.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Book", "title", booksInDto.getTitle());
        }

        book = BooksMapper.mapToBook(booksInDto, book, categoriesRepository);

        Books savedBook = booksRepository.save(book);

        BooksOutDto booksOutDto = BooksMapper.mapToBookOutDTO(savedBook, new BooksOutDto());

        return booksOutDto;

    }


    //delete method
    @Override
    public BooksOutDto deleteBookById(Long id) {

        Books book = booksRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", id.toString())
        );


        boolean isBookIssued = issuancesRepository.existsByBooksIdAndStatus(book.getId(), "ISSUED");
        if (isBookIssued) {

            throw new MethodNotAllowedException("Cannot delete book because this book is issued to someone.");
        }

        booksRepository.deleteById((book.getId()));

        BooksOutDto booksOutDto = BooksMapper.mapToBookOutDTO(book, new BooksOutDto());
        return booksOutDto;

    }
    @Override
    @Transactional
    public BooksOutDto deleteBookByTitle(String title) {
        // Fetch the book by title
        Books book = booksRepository.findByTitleIgnoreCase(title).orElseThrow(
                () -> new ResourceNotFoundException("Book", "title", title)
        );


        boolean isBookIssued = issuancesRepository.existsByBooksIdAndStatus(book.getId(), "ISSUED");
        if (isBookIssued) {

            throw new EntityConstraintViolationException("Cannot delete book as it is currently issued.");
        }


        booksRepository.deleteById(book.getId());

        // Map the deleted book to DTO and return
        BooksOutDto booksOutDto = BooksMapper.mapToBookOutDTO(book, new BooksOutDto());
        return booksOutDto;
    }



}
