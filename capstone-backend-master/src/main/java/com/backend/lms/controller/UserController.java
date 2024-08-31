package com.backend.lms.controller;

import com.backend.lms.dto.users.RegisterRequestDto;
import com.backend.lms.dto.users.UserDto;
import com.backend.lms.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    private final IUserService iUserService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        if (page == null || size == null) {
            List<UserDto> userDtoList = iUserService.getAllUsers(Sort.by(sortBy));
            return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<UserDto> userDtoPage = iUserService.getUsers(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(userDtoPage);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        UserDto savedUser = iUserService.registerUser(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/user/{mobileNumber}")
    public ResponseEntity<UserDto> getUser(@PathVariable String mobileNumber) {
        UserDto userDto = iUserService.getUserByMobile(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @GetMapping("/user-count")
    public ResponseEntity<Long> getUserCount() {
        Long userCount = iUserService.getUserCount();
        return ResponseEntity.status(200).body(userCount);
    }

    @DeleteMapping("/user/{mobileNumber}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable String mobileNumber) {
        UserDto userDto = iUserService.deleteUserByMobile(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PutMapping("/user/{mobileNumber}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String mobileNumber, @RequestBody RegisterRequestDto registerRequestDto) {
        UserDto userDto = iUserService.updateUser(mobileNumber, registerRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

}