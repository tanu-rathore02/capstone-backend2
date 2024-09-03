package com.backend.lms.service.impl;



import com.backend.lms.constants.JWTConstants;
import com.backend.lms.dto.users.RegisterRequestDto;
import com.backend.lms.dto.users.UserDto;
import com.backend.lms.model.Users;
import com.backend.lms.mapper.UserMapper;
import com.backend.lms.repository.UsersRepository;
import com.backend.lms.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    @Override
    public List<UserDto> getAllUsers(Sort sort) {
        return userRepository.findByRole("ROLE_USER", sort).stream()
                .map(user -> UserMapper.mapToUserDto(user, new UserDto()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> getUsers(Pageable pageable, String search) {
        Page<Users> userPage;
        if (search != null && !search.isEmpty()) {
            userPage = userRepository.findByMobileNumberContainingIgnoreCaseAndRole(search, "ROLE_USER", pageable);
        } else {
            userPage = userRepository.findByRole("ROLE_USER", pageable);
        }

        return userPage.map(user -> UserMapper.mapToUserDto(user, new UserDto()));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + email)
        );

        UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());

        return userDto;

    }

    @Override
    public UserDto getUserByMobile(String mobileNumber) {
        Users user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + mobileNumber)
        );

        UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());

        return userDto;

    }

    @Override
    public List<UserDto> getAllUsers() {
        List<Users> userList = userRepository.findAll();
        List<UserDto> userDTOList = new ArrayList<>();

        userList.forEach(user -> userDTOList.add(UserMapper.mapToUserDto(user, new UserDto())));
        return userDTOList;
    }

    @Override
    public Long getUserCount() {
        Long userCount = userRepository.count();
        return userCount;
    }


    @Override
    public UserDto deleteUserByMobile(String mobileNumber) {
        Users user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for " + mobileNumber)
        );

        userRepository.deleteById(user.getId());

        UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());

        return userDto;
    }

    @Override
    public UserDto registerUser(RegisterRequestDto registerRequestDto) {
        Users user = UserMapper.mapToUser(registerRequestDto, new Users());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        Users savedUser = userRepository.save(user);
        UserDto userDtO = UserMapper.mapToUserDto(savedUser, new UserDto());
        return  userDtO;
    }

    @Override
    public UserDto updateUser(String mobileNumber, RegisterRequestDto registerRequestDto) {
        Users user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new UsernameNotFoundException("User not found for mobile no. " + mobileNumber)
        );

        user = UserMapper.mapToUser(registerRequestDto, user);
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        Users updatedUser = userRepository.save(user);

        UserDto userDto = UserMapper.mapToUserDto(updatedUser, new UserDto());

        return  userDto;

    }

    @Override
    public UserDto getUserByToken(String jwt) {
        String secret = env.getProperty(JWTConstants.JWT_SECRET_KEY, JWTConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        String username = String.valueOf(claims.get("username"));

        if (username.contains("@")) {
            Users user = userRepository.findByEmail(username).orElseThrow(
                    () -> new BadCredentialsException("Invalid Token received!")
            );

            UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());
            userDto.setToken(jwt);
            return userDto;
        } else {
            Users user = userRepository.findByMobileNumber(username).orElseThrow(
                    () -> new BadCredentialsException("Invalid Token received!")
            );

            UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());
            userDto.setToken(jwt);
            return userDto;
        }
    }

}