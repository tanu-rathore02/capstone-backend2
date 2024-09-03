package com.backend.lms.mapper;



import com.backend.lms.dto.users.RegisterRequestDto;
import com.backend.lms.dto.users.UserDto;
import com.backend.lms.model.Users;

public final class UserMapper {

    public static UserDto mapToUserDto(Users user, UserDto userDto) {
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setMobileNumber(user.getMobileNumber());
        userDto.setRole(user.getRole());
        userDto.setToken(null);

        return userDto;
    }

    public static Users mapToUser(UserDto userDTO, Users user) {
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setRole(userDTO.getRole());

        return user;
    }

    public static Users mapToUser (RegisterRequestDto registerRequestDto, Users user) {
        user.setName(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        user.setMobileNumber(registerRequestDto.getMobileNumber());
        user.setRole(registerRequestDto.getRole());

        if (registerRequestDto.getPassword() != null || !registerRequestDto.getPassword().isEmpty()) {
            user.setPassword(registerRequestDto.getPassword());
        }

        return user;
    }


}