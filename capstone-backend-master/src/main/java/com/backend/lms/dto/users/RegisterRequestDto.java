package com.backend.lms.dto.users;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequestDto {

    @NotEmpty(message = "Name can not be a null or empty")
    private String name;

    @NotEmpty(message = "Email can not be a null or empty")
    private String email;

    @NotEmpty(message = "Mobile number can not be a null or empty")
    private String mobileNumber;


    private String password;

    @NotEmpty(message = "Role can not be a null or empty")
    private String role = "ROLE_USER";


}