package com.backend.lms.service.impl;


import com.backend.lms.dto.categories.CategoriesDto;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.mapper.CategoriesMapper;
import com.backend.lms.model.Categories;
import com.backend.lms.repository.BooksRepository;
import com.backend.lms.repository.CategoriesRepository;
import com.backend.lms.service.ICategoriesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImplement implements ICategoriesService {
    private  final CategoriesRepository categoriesRepository;
    private final BooksRepository booksRepository;


   //get APIs
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
       }else {
           categoriesPage = categoriesRepository.findAll(pageable);
       }
       return categoriesPage.map(categories -> CategoriesMapper.mapToCategoriesDto(categories, new CategoriesDto()));
   }


    @Override
    public Long getCategoriesCount() {
       return categoriesRepository.count();
    }


    public CategoriesDto getCategoryById(Long id){
       Categories categories = categoriesRepository.findById(id).orElseThrow(
               () -> new ResourceNotFoundException("Category", "id", id.toString())
       );
       CategoriesDto categoriesDto = CategoriesMapper.mapToCategoriesDto(categories, new CategoriesDto());
       return categoriesDto;
    }

    @Override
    public CategoriesDto getCategoryByName(String categoryName){
       Categories categories = categoriesRepository.findByCategoryName(categoryName).orElseThrow(
               () -> new ResourceNotFoundException("Category", "name", categoryName)
       );

       CategoriesDto categoriesDto = CategoriesMapper.mapToCategoriesDto(categories, new CategoriesDto());
       return categoriesDto;
    }

    //Post API
    @Override
    public CategoriesDto createCategory(CategoriesDto categoriesDto){
       Categories category = CategoriesMapper.mapToCategories(categoriesDto, new Categories());
       Categories savedCategory = categoriesRepository.save(category);

       CategoriesDto categoryDtoPosted = CategoriesMapper.mapToCategoriesDto(savedCategory, new CategoriesDto());
       return categoryDtoPosted;
    }

    //Put API
    @Override
    public CategoriesDto updateCategory(Long id, CategoriesDto categoriesDto){
       Categories category = categoriesRepository.findById(id).orElseThrow(
               () -> new ResourceNotFoundException("Category" , "id", id.toString())
       );
        category = CategoriesMapper.mapToCategories(categoriesDto, category);
        Categories updatedCategory = categoriesRepository.save(category);

        CategoriesDto categoryDtoUpdated = CategoriesMapper.mapToCategoriesDto(updatedCategory, new CategoriesDto());

        return categoryDtoUpdated;
   }


    @Override
    @Transactional
    public CategoriesDto deleteCategoryById(Long id) {
        Categories category = categoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );

        booksRepository.deleteByCategories(category);

        categoriesRepository.deleteById(id);

        CategoriesDto categoryDtoDeleted = CategoriesMapper.mapToCategoriesDto(category, new CategoriesDto());
        return categoryDtoDeleted;
    }
    @Override
    public CategoriesDto deleteCategoryByName(String name) {
        Categories category = categoriesRepository.findByCategoryName(name).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "name", name)
        );
        categoriesRepository.deleteById(category.getId());
        CategoriesDto categoryDtoDeleted = CategoriesMapper.mapToCategoriesDto(category, new CategoriesDto());

        return categoryDtoDeleted;
    }
}



