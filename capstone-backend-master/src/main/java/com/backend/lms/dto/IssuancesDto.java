package com.backend.lms.dto;

import java.time.LocalDateTime;

public class IssuancesDto {
    private int id;
    private LocalDateTime issueDate;
    private LocalDateTime returnDate;
    private String status;
    private int userId;
    private int bookId;

    //constructor

    public IssuancesDto() {
    }

    public IssuancesDto(int id, LocalDateTime issueDate, LocalDateTime returnDate, String status, int userId, int bookId) {
        this.id = id;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.userId = userId;
        this.bookId = bookId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}