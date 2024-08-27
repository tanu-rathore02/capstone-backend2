package com.backend.lms.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="issuances")
public class Issuances {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String status;
    private LocalDateTime issueDate;
    private LocalDateTime returnDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name= "book_id")
    private Books books;

    //constructors
    public Issuances() {
    }

    public Issuances(int id, String status, LocalDateTime issueDate, LocalDateTime returnDate, Users users, Books books) {
        this.id = id;
        this.status = status;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.users = users;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }
}