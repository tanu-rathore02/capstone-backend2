package com.backend.lms.dto.books;


import com.backend.lms.model.Users;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BooksHistoryDto {
    private Long id;
    private Users user;
    private LocalDateTime issueTime;
    private LocalDateTime expectedReturnTime;
    private LocalDateTime actualReturnTime;
    private String status;
    private String type;

}