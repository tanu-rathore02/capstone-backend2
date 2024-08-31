package com.backend.lms.dto.Issuances;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
public class IssuanceInDto {

    @NotEmpty(message = "User can not be a null or empty")
    private Long userId;

    @NotEmpty(message = "Book can not be a null or empty")
    private Long bookId;

    @NotEmpty(message = "Return time can not be a null or empty")
    private LocalDateTime returnDate;

    @NotEmpty(message = "Status can not be a null or empty")
    private String status;

    @NotEmpty(message = "Issuance type can not be a null or empty")
    private String issuanceType;

}
