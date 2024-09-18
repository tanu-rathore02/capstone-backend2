package com.backend.lms.dto.Issuances;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
public class IssuanceInDto {

    @NotNull(message = "User cannot be null")
    private Long userId;

    @NotNull(message = "Book cannot be null")
    private Long bookId;

    @NotNull(message = "Return time cannot be null")
    private LocalDateTime returnDate;

    @NotEmpty(message = "Status cannot be null")
    private String status;

    @NotEmpty(message = "Issuance type cannot be null")
    private String issuanceType;
}

