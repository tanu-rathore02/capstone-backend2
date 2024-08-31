package com.backend.lms.dto.Issuances;


import com.backend.lms.dto.books.BooksOutDto;
import com.backend.lms.dto.users.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IssuanceOutDto {

    private Long id;

    private UserDto users;

    private BooksOutDto books;

    private LocalDateTime issueDate;

    private LocalDateTime returnDate;

    private String status;

    private String issuanceType;

}
