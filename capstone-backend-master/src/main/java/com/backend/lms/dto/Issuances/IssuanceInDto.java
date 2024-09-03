package com.backend.lms.dto.Issuances;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
public class IssuanceInDto {

    @NotEmpty(message = "User can not be null")
    private Long userId;

    @NotEmpty(message = "Book can not be  null")
    private Long bookId;

    @NotEmpty(message = "Return time can not be  null")
    private LocalDateTime returnDate;

    @NotEmpty(message = "Status can not be null ")
    private String status;

    @NotEmpty(message = "Issuance type can not be  null")
    private String issuanceType;

}
