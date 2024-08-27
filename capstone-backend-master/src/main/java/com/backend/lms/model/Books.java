package com.backend.lms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String author;
    private int availability;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories categories;

    // Constructors
    public Books() {
    }

    public Books(String title, String author, int availability, Categories categories) {
        this.title = title;
        this.author = author;
        this.availability = availability;
        this.categories = categories;
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

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }
}
