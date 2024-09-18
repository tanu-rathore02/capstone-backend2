package com.backend.lms.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LoginResponseDto {

    private String name;

    private String mobileNumber;

    private String role;

    private String token;

    private Long id;

}