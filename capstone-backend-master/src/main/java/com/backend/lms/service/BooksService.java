
package com.backend.lms.service;

import com.backend.lms.dto.BooksDto;
import com.backend.lms.model.Books;
import com.backend.lms.model.Categories;
import com.backend.lms.repository.BooksRepository;
import com.backend.lms.repository.CategoriesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    @Autowired
    private BooksRepository bookRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    // DTO mapping - method to map entity to DTO
    private BooksDto mapToDto(Books books) {
        BooksDto booksDto = new BooksDto();
        booksDto.setId(books.getId());
        booksDto.setTitle(books.getTitle());
        booksDto.setAuthor(books.getAuthor());
        booksDto.setAvailability(books.getAvailability());
        booksDto.setCategoryId(books.getCategories().getId());  // Only set the categoryId
        return booksDto;
    }

    // Entity mapping - method to map DTO to entity
    private Books mapToEntity(BooksDto booksDto) {
        Books books = new Books();
        books.setTitle(booksDto.getTitle());
        books.setAuthor(booksDto.getAuthor());
        books.setAvailability(booksDto.getAvailability());

        // Fetch the category entity using the categoryId from the DTO
        Categories category = categoriesRepository.findById(booksDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        books.setCategories(category);

        return books;
    }

    // Get all books
    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a book by id
    public BooksDto getBooksById(int id) {
        Books books = bookRepository.findById(id).orElse(null);
        if (books != null) {
            return mapToDto(books);
        } else {
            return null;
        }
    }

    // Create a new book
    public BooksDto createBooks(BooksDto booksDto) {
        Books books = mapToEntity(booksDto);
        Books savedBooks = bookRepository.save(books);
        return mapToDto(savedBooks);
    }

    // Update a book
    public BooksDto updateBooks(int id, BooksDto booksDto) {
        Books books = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        books.setTitle(booksDto.getTitle());
        books.setAuthor(booksDto.getAuthor());
        books.setAvailability(booksDto.getAvailability());

        // Fetch the category entity using the categoryId from the DTO
        Categories category = categoriesRepository.findById(booksDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        books.setCategories(category);

        Books updatedBooks = bookRepository.save(books);
        return mapToDto(updatedBooks);
    }

    // Delete a book
    public void deleteBooks(int id) {
        Books books = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        bookRepository.delete(books);
    }
}
