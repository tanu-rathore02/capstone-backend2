package com.backend.lms.controller;

import com.backend.lms.constants.JWTConstants;
import com.backend.lms.dto.users.UserDto;
import com.backend.lms.dto.auth.LoginRequestDto;
import com.backend.lms.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final IUserService iUserService;


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String jwt = "";
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        );

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        if (null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                String secret = env.getProperty(JWTConstants.JWT_SECRET_KEY, JWTConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder()
                        .setIssuer("LMS")
                        .setSubject("JWT Token")
                        .claim("username", authenticationResponse.getName())
                        .claim("authorities", authenticationResponse.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(",")))
                        .setIssuedAt(new java.util.Date())
                        .setExpiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                        .signWith(secretKey)
                        .compact();
            }
        }

        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authenticationResponse.getAuthorities();
        List<String> roles = grantedAuthorities.stream()
                .map(GrantedAuthority::toString)
                .collect(Collectors.toList());

        String username = loginRequestDto.getUsername();
        UserDto userDTO;

        if (username.contains("@")) {
            userDTO = iUserService.getUserByEmail(username);
        } else {
            userDTO = iUserService.getUserByMobile(username);
        }

        userDTO.setToken(jwt);
        userDTO.setRole(roles.get(0));

        return ResponseEntity.status(HttpStatus.OK)
                .header(JWTConstants.JWT_HEADER, jwt)
                .body(userDTO);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserDto> currentUser(@RequestHeader("Authorization") String token) {
        UserDto user = iUserService.getUserByToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(user);


    }

}
