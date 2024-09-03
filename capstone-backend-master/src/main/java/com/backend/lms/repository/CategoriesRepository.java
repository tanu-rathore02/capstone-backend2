package com.backend.lms.repository;


import com.backend.lms.model.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Optional<Categories> findByCategoryName(String categoryName);

    Page<Categories> findByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
}
