package com.backend.lms.controller;

import com.backend.lms.dto.BooksDto;
import com.backend.lms.model.Books;
import com.backend.lms.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BooksController {

    @Autowired
    private BooksService booksService;

    //Get all books
    @GetMapping("/allBooks")
    public ResponseEntity<List<Books>> getAllBooks() {
        List<Books> list =booksService.getAllBooks();
        if(list.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(list);
    }

    //get a book by id
    @GetMapping("/getBooks/{id}")
    public ResponseEntity<BooksDto> getBooksById(@PathVariable int id) {
        BooksDto bookDto = booksService.getBooksById(id);
        if(bookDto==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(bookDto);
    }

    //creating a new book
    @PostMapping("/createBooks")
    public ResponseEntity<BooksDto> createBooks(@RequestBody BooksDto bookDto) {
        try {
            BooksDto createdBook = booksService.createBooks(bookDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //updating a book
    @PutMapping("/updateBooks/{id}")
    public ResponseEntity<BooksDto> updateBooks(@PathVariable int id, @RequestBody BooksDto bookDto) {
        try
        {
            BooksDto updatedBook = booksService.updateBooks(id, bookDto);
            return ResponseEntity.ok().body(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    //delete a book
    @DeleteMapping("/deleteBooks/{id}")
    public ResponseEntity<Void> deleteBooks(@PathVariable int id) {
        try{
            booksService.deleteBooks(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
