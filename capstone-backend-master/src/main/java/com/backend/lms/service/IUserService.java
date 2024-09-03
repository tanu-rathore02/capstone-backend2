package com.backend.lms.service;

import com.backend.lms.dto.users.RegisterRequestDto;
import com.backend.lms.dto.users.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    List<UserDto> getAllUsers(Sort sort);
    Page<UserDto> getUsers(Pageable pageable, String search);

    UserDto getUserByEmail(String email);
    UserDto getUserByMobile(String mobileNumber);
    List<UserDto> getAllUsers();
    Long getUserCount();


    UserDto deleteUserByMobile(String mobileNumber);

    UserDto registerUser(RegisterRequestDto registerRequestDto);

    UserDto updateUser(String mobileNumber, RegisterRequestDto registerRequestDto);

    UserDto getUserByToken(String token);

}
