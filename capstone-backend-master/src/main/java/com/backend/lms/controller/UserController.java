package com.backend.lms.controller;
import com.backend.lms.dto.response.ResponseDto;
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
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<UserDto> userDtoPage = iUserService.getUsers(pageable, search);
        return ResponseEntity.status(HttpStatus.OK).body(userDtoPage);
    }

    @GetMapping("/allUsersForDropDown")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        List<UserDto> users = iUserService.getAllUsers(sort);
        return ResponseEntity.status(HttpStatus.OK).body(users);
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

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody RegisterRequestDto registerRequestDto, UserDto userDto) {
        UserDto savedUser = iUserService.registerUser(registerRequestDto, userDto);

        ResponseDto responseDto = new ResponseDto("201", "User registered successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @DeleteMapping("/user/deleteUser/{mobileNumber}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable String mobileNumber) {
        UserDto userDto = iUserService.deleteUserByMobile(mobileNumber);

        ResponseDto responseDto = new ResponseDto("200", "User deleted successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/user/updateUser/{mobileNumber}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable String mobileNumber, @RequestBody RegisterRequestDto registerRequestDto, Long id) {
        UserDto userDto = iUserService.updateUser(mobileNumber, registerRequestDto, id);
        ResponseDto responseDto = new ResponseDto("200", "User details updated successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
}