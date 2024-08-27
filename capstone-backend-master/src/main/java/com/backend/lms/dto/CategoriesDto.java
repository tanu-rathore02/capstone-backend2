package com.backend.lms.dto;

public class CategoriesDto {
    private int id;
    private String categoryName;


    public CategoriesDto(){}

    public CategoriesDto(String categoryName, String categoryImage){
        this.categoryName = categoryName;

    }

    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
