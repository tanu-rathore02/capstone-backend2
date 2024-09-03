package com.backend.lms.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ResponseDto {

    private String statusCode;

    private String statusMsg;

}