package com.backend.lms.service.impl;



import com.backend.lms.constants.JWTConstants;
import com.backend.lms.dto.users.RegisterRequestDto;
import com.backend.lms.dto.users.UserDto;
import com.backend.lms.exception.EntityConstraintViolationException;
import com.backend.lms.exception.MethodNotAllowedException;
import com.backend.lms.exception.ResourceAlreadyExistsException;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.model.Categories;
import com.backend.lms.model.Users;
import com.backend.lms.mapper.UserMapper;
import com.backend.lms.repository.IssuancesRepository;
import com.backend.lms.repository.UsersRepository;
import com.backend.lms.service.ISMSService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;
    private final ISMSService ismsService;
    private final IssuancesRepository issuancesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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
                () -> new ResourceNotFoundException("User", "mobileNumber", mobileNumber)
        );

        boolean isUserHavingIssuedBooks = issuancesRepository.existsByUsersIdAndStatus(user.getId(), "ISSUED");
        if (isUserHavingIssuedBooks) {

            throw new MethodNotAllowedException("Cannot delete user because one or more books are issued.");
        }


        userRepository.deleteById(user.getId());

        UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());

        return userDto;
    }

    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public String generatePassword(String rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public UserDto registerUser(RegisterRequestDto registerRequestDto, UserDto userDto) {
        Optional<Users> users = userRepository.findByMobileNumber(registerRequestDto.getMobileNumber());
        System.out.println(users);
        if (users.isPresent()) {
            throw new ResourceAlreadyExistsException("User", "mobile number", userDto.getMobileNumber());
        }



        Users user = UserMapper.mapToUser(registerRequestDto, new Users());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        String randomPassword = generateRandomPassword();

        String encryptedPassword = generatePassword(randomPassword);
        user.setPassword("{bcrypt}" + encryptedPassword);

        Users savedUser = userRepository.save(user);


        String message = String.format( "\nWelcome %s\n" +
                        "You have successfully registered to Library\n" +
                        "These are your login credentials\n" +
                        "Username: %s (OR) %s\n" +
                        "Password: %s",
                savedUser.getName(),
                savedUser.getMobileNumber(),
                savedUser.getEmail(),
                user.getPassword());

//        ismsService.verifyNumber(savedUser.getMobileNumber());
//        ismsService.sendSms(savedUser.getMobileNumber(), message);
        UserDto userDtO = UserMapper.mapToUserDto(savedUser, new UserDto());
        return  userDtO;
    }


    @Override
    public UserDto updateUser(String mobileNumber, RegisterRequestDto registerRequestDto, Long id) {
        // Fetch the existing user by the provided mobile number
        Users user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("User", "mobile number", mobileNumber)
        );

        // If the mobile number in the request is different, check for conflicts with other users
        if (registerRequestDto.getMobileNumber() != null && !registerRequestDto.getMobileNumber().equals(user.getMobileNumber())) {
            Optional<Users> existingUser = userRepository.findByMobileNumber(registerRequestDto.getMobileNumber());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new ResourceAlreadyExistsException("User", "mobile number", registerRequestDto.getMobileNumber());
            }
            // Update the mobile number if no conflict
            user.setMobileNumber(registerRequestDto.getMobileNumber());
        }

        // Update only the other fields that are present in the request
        if (registerRequestDto.getName() != null) {
            user.setName(registerRequestDto.getName());
        }
        if (registerRequestDto.getEmail() != null) {
            user.setEmail(registerRequestDto.getEmail());
        }
        if (registerRequestDto.getPassword() != null && !registerRequestDto.getPassword().isEmpty()) {
            // If new password is provided, encode it
            user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        }

        // Save the updated user details
        Users updatedUser = userRepository.save(user);

        // Return the updated UserDto
        return UserMapper.mapToUserDto(updatedUser, new UserDto());
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