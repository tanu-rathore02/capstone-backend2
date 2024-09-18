package com.backend.lms.controller;

import com.backend.lms.dto.books.BooksInDto;
import com.backend.lms.dto.books.BooksOutDto;
import com.backend.lms.dto.categories.CategoriesDto;
import com.backend.lms.dto.response.ResponseDto;
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
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<BooksOutDto> bookDtoPage = iBooksService.getBooksPaginated(pageable, search);
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoPage);
    }

    @GetMapping("/allForDropDown")
    public ResponseEntity<List<BooksOutDto>> getAllBooks(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        List<BooksOutDto> books = iBooksService.getAllBooks(sort);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }


    @GetMapping("/title-count")
    public ResponseEntity<Long> getBookTitleCount() {
        Long count = iBooksService.getBookTitleCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

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

    @GetMapping("/getBook/{id}")
    public ResponseEntity<BooksOutDto> getBookById(@PathVariable Long id) {
        BooksOutDto bookOutDTO = iBooksService.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BooksOutDto> getBookByTitle(@PathVariable String title) {
        BooksOutDto bookOutDTO = iBooksService.getBookByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(bookOutDTO);
    }

    //Post API
    @PostMapping("/createBook")
    public ResponseEntity<ResponseDto> createBook(@RequestBody BooksInDto booksInDto) {
        BooksOutDto booksOutDto = iBooksService.createBook(booksInDto);

        ResponseDto responseDto = new ResponseDto("200", "Book created successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //Update API
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<ResponseDto> updateBook(@PathVariable Long id, @RequestBody BooksInDto booksInDto) {
        BooksOutDto bookOutDTO = iBooksService.updateBook(id, booksInDto);

        ResponseDto responseDto = new ResponseDto("200", "Book updated successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //Delete API
    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<ResponseDto> deleteBookById(@PathVariable Long id) {
        BooksOutDto booksOutDto = iBooksService.deleteBookById(id);

        ResponseDto responseDto = new ResponseDto("200", "Book deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/deleteBook/title/{title}")
    public ResponseEntity<ResponseDto> deleteBookByTitle(@PathVariable String title) {
        BooksOutDto booksOutDto = iBooksService.deleteBookByTitle(title);

        ResponseDto responseDto = new ResponseDto("200", "Book deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
