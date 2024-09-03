package com.backend.lms.repository;

import com.backend.lms.model.Books;
import com.backend.lms.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    Page<Books> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Updated method to use Categories entity
    List<Books> findAllByCategories(Categories categories);

    Optional<Books> findByTitle(String title);

    List<Books> findAllByAuthor(String author);

    void deleteByCategories(Categories category);

}
