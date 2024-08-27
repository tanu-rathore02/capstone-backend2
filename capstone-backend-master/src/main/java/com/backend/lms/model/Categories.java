package com.backend.lms.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="categories")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String categoryName;




    //constructor
    public Categories(){}

    public Categories(String categoryName, String categoryImage){
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
