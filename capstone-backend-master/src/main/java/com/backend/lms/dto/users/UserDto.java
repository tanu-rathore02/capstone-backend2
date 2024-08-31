package com.backend.lms.dto.users;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotEmpty(message = "Name can not be a null or empty")
    private String name;

    @NotEmpty(message = "Mobile number can not be a null or empty")
    private String mobileNumber;

    @NotEmpty(message = "Email can not be a null or empty")
    private String email;

    private String role;

    private String token;

}