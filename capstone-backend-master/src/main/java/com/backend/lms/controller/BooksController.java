package com.backend.lms.controller;

import com.backend.lms.dto.books.BooksInDto;
import com.backend.lms.dto.books.BooksOutDto;
import com.backend.lms.service.IBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/books", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BooksController {

   private final IBooksService iBooksService;


    //Get APIs
    @GetMapping("/allBooks")
    public ResponseEntity<?> getBooks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        if (page == null || size == null) {
            // Fetch all categories without pagination
            List<BooksOutDto> bookOutDTOList = iBooksService.getAllBooks(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(bookOutDTOList);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<BooksOutDto> bookDtoPage = iBooksService.getBooksPaginated(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(bookDtoPage);
        }
    }

    @GetMapping("/title-count")
    public ResponseEntity<Long> getBookTitleCount() {
        Long count = iBooksService.getBookTitleCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

//    @GetMapping("/total-count")
//    public ResponseEntity<Long> getTotalBooksCount() {
//        Long count = iBooksService.getTotalBooksCount();
//        return ResponseEntity.status(HttpStatus.OK).body(count);
//    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BooksOutDto>> getBooksByAuthor(@PathVariable String author) {
        List<BooksOutDto> bookOutDTOList = iBooksService.getBooksByAuthor(author);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTOList);
    }

    @GetMapping("/categoryId/{categoryId}")
    public ResponseEntity<List<BooksOutDto>> getBooksByCategoryId(@PathVariable Long categoryId) {
        List<BooksOutDto> bookOutDTOList = iBooksService.getBooksByCategoryId(categoryId);
        return ResponseEntity.status(200).body(bookOutDTOList);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BooksOutDto> getBookById(@PathVariable Long id) {
        BooksOutDto bookOutDTO = iBooksService.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BooksOutDto> getBookByTitle(@PathVariable String title) {
        BooksOutDto bookOutDTO = iBooksService.getBookByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

    //post api
    @PostMapping("/book")
    public ResponseEntity<BooksOutDto> createBook(@RequestBody BooksInDto booksInDto) {
        BooksOutDto booksOutDto = iBooksService.createBook(booksInDto);
        return ResponseEntity.status(HttpStatus.OK).body(booksOutDto);
    }

    //updating a book
    @PutMapping("/book/{id}")
    public ResponseEntity<BooksOutDto> updateBook(@PathVariable Long id, @RequestBody BooksInDto booksInDto) {
        BooksOutDto bookOutDTO = iBooksService.updateBook(id, booksInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookOutDTO);
    }

    //delete a book
    public ResponseEntity<BooksOutDto> deleteBookById(@PathVariable Long id) {
        BooksOutDto booksOutDto = iBooksService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(booksOutDto);
    }
}
