package com.backend.lms.dto.users;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequestDto {

    private String name;

    private String email;

    private String mobileNumber;

    private String password;

    private String role = "ROLE_USER";


}