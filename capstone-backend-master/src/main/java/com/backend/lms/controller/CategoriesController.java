package com.backend.lms.controller;

import com.backend.lms.dto.categories.CategoriesDto;
import com.backend.lms.dto.response.ResponseDto;
import com.backend.lms.service.ICategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CategoriesController {

    private final ICategoriesService iCategoriesService;


    @GetMapping("/allCategories")
    public ResponseEntity<?> getCategories(
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
        Page<CategoriesDto> categoryDTOPage = iCategoriesService.getCategoriesPaginated(pageable, search);

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOPage);
    }

    @GetMapping("/allForDropDown")
    public ResponseEntity<List<CategoriesDto>> getAllCategories(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        List<CategoriesDto> categories = iCategoriesService.getAllCategories(sort);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }


    @GetMapping("/categoryCount")
    public ResponseEntity<Long> getCategoryCount() {
        Long categoryCount = iCategoriesService.getCategoriesCount();
        return ResponseEntity.status(HttpStatus.OK).body(categoryCount);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoriesDto> getCategory(@PathVariable Long id) {
        CategoriesDto categoriesDto = iCategoriesService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoriesDto);
    }

    @GetMapping("/category/name/{name}")
    public ResponseEntity<CategoriesDto> getCategoryByName(@PathVariable String name) {
        CategoriesDto categoriesDto = iCategoriesService.getCategoryByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(categoriesDto);
    }

    @PostMapping("/createCategory")
    public ResponseEntity<ResponseDto> createCategory(@RequestBody CategoriesDto categoryDTO) {
        CategoriesDto savedCategoryDTO = iCategoriesService.createCategory(categoryDTO);

        ResponseDto responseDto = new ResponseDto("200", "Category created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<ResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoriesDto categoryDTO) {
        CategoriesDto updatedCategoryDTO = iCategoriesService.updateCategory(id, categoryDTO);

        ResponseDto responseDto = new ResponseDto("200", "Category updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<ResponseDto> deleteCategory(@PathVariable Long id) {

        CategoriesDto categoryDTO = iCategoriesService.deleteCategoryById(id);

        ResponseDto responseDto = new ResponseDto("200", "Category deleted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/deleteCategory/name/{name}")
    public ResponseEntity<ResponseDto> deleteCategoryByName(@PathVariable String name) {
        CategoriesDto categoryDTO = iCategoriesService.deleteCategoryByName(name);

        ResponseDto responseDto = new ResponseDto("200", "Category deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
