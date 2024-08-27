package com.backend.lms.dto;

public class BooksDto {

    private int id;
    private String title;
    private String author;
    private int availability;
    private String bookImage;
    private int categoryId;  // Replacing CategoriesDto with just categoryId

    // Constructors
    public BooksDto() {
    }

    public BooksDto(String title, String author, int availability, String bookImage, int categoryId) {
        this.title = title;
        this.author = author;
        this.availability = availability;
        this.bookImage = bookImage;
        this.categoryId = categoryId;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
