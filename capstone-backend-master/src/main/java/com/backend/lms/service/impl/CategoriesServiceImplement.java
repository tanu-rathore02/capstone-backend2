package com.backend.lms.service.impl;

import com.backend.lms.dto.categories.CategoriesDto;
import com.backend.lms.exception.EntityConstraintViolationException;
import com.backend.lms.exception.MethodNotAllowedException;
import com.backend.lms.exception.ResourceAlreadyExistsException;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.CategoriesMapper;
import com.backend.lms.model.Books;
import com.backend.lms.model.Categories;
import com.backend.lms.repository.BooksRepository;
import com.backend.lms.repository.CategoriesRepository;
import com.backend.lms.repository.IssuancesRepository;
import com.backend.lms.service.ICategoriesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImplement implements ICategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final BooksRepository booksRepository;
    private final IssuancesRepository issuancesRepository;

    // Get APIs
    @Override
    public List<CategoriesDto> getAllCategories(Sort sort){
        return categoriesRepository.findAll(sort).stream()
                .map(categories -> CategoriesMapper.mapToCategoriesDto(categories, new CategoriesDto()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoriesDto> getCategoriesPaginated(Pageable pageable, String search){
        Page<Categories> categoriesPage;
        if(search != null && !search.isEmpty()){
            categoriesPage = categoriesRepository.findByCategoryNameContainingIgnoreCase(search, pageable);
        } else {
            categoriesPage = categoriesRepository.findAll(pageable);
        }
        return categoriesPage.map(categories -> CategoriesMapper.mapToCategoriesDto(categories, new CategoriesDto()));
    }

    @Override
    public Long getCategoriesCount() {
        return categoriesRepository.count();
    }

    @Override
    public CategoriesDto getCategoryById(Long id){
        Categories categories = categoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );
        return CategoriesMapper.mapToCategoriesDto(categories, new CategoriesDto());
    }

    @Override
    public CategoriesDto getCategoryByName(String categoryName){
        Categories categories = categoriesRepository.findByCategoryNameIgnoreCase(categoryName).orElseThrow(
                () -> new ResourceNotFoundException("Category", "name", categoryName)
        );
        return CategoriesMapper.mapToCategoriesDto(categories, new CategoriesDto());
    }

    // Post API
    @Override
    public CategoriesDto createCategory(CategoriesDto categoriesDto) {
        if (categoriesRepository.findByCategoryNameIgnoreCase(categoriesDto.getCategoryName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Category", "categoryName", categoriesDto.getCategoryName());
        }
        Categories category = CategoriesMapper.mapToCategories(categoriesDto, new Categories());
        Categories savedCategory = categoriesRepository.save(category);
        return CategoriesMapper.mapToCategoriesDto(savedCategory, new CategoriesDto());
    }

    // Put API
    @Override
    public CategoriesDto updateCategory(Long id, CategoriesDto categoriesDto) {
        Categories category = categoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );

        Optional<Categories> existingCategory = categoriesRepository.findByCategoryNameIgnoreCase(categoriesDto.getCategoryName());
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Category", "categoryName", categoriesDto.getCategoryName());
        }

        category = CategoriesMapper.mapToCategories(categoriesDto, category);
        Categories updatedCategory = categoriesRepository.save(category);
        return CategoriesMapper.mapToCategoriesDto(updatedCategory, new CategoriesDto());
    }

    @Override
    @Transactional
    public CategoriesDto deleteCategoryById(Long id) {
        Categories category = categoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );

        List<Books> books = booksRepository.findByCategoriesId(category.getId());
        for (Books book : books) {
            if (issuancesRepository.existsByBooksIdAndStatus(book.getId(), "ISSUED")) {
                throw new MethodNotAllowedException("Cannot delete category because one or more books are issued.");
            }
        }

        booksRepository.deleteByCategories(category);
        categoriesRepository.deleteById(id);
        return CategoriesMapper.mapToCategoriesDto(category, new CategoriesDto());
    }

    @Override
    @Transactional
    public CategoriesDto deleteCategoryByName(String name) {
        Categories category = categoriesRepository.findByCategoryNameIgnoreCase(name).orElseThrow(
                () -> new ResourceNotFoundException("Category", "name", name)
        );

        List<Books> books = booksRepository.findByCategoriesId(category.getId());
        for (Books book : books) {
            if (issuancesRepository.existsByBooksIdAndStatus(book.getId(), "ISSUED")) {
                throw new EntityConstraintViolationException("Cannot delete category because one or more books are issued.");
            }
        }

        booksRepository.deleteByCategories_Id(category.getId());
        categoriesRepository.deleteById(category.getId());
        return CategoriesMapper.mapToCategoriesDto(category, new CategoriesDto());
    }
}
