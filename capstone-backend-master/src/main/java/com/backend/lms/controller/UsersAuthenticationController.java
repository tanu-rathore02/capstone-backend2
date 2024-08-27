package com.backend.lms.controller;

import com.backend.lms.dto.LoginUserDto;
import com.backend.lms.dto.RefreshTokenRequest;
import com.backend.lms.dto.RegisterUserDto;
import com.backend.lms.model.Role;
import com.backend.lms.model.Users;
import com.backend.lms.responses.JwtResponse;
import com.backend.lms.service.UsersAuthenticationService;
import com.backend.lms.securityConfig.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class UsersAuthenticationController {
    private final JwtService jwtService;
    private final UsersAuthenticationService usersAuthenticationService;

    public UsersAuthenticationController(JwtService jwtService, UsersAuthenticationService usersAuthenticationService) {
        this.jwtService = jwtService;
        this.usersAuthenticationService = usersAuthenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Users> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            Users registeredUser = usersAuthenticationService.signup(registerUserDto);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            Users user = usersAuthenticationService.authenticate(loginUserDto);
            if (user == null) {
                return ResponseEntity.status(401).body(null);
            }

            UserDetails authenticatedUser;
            if (user.getRole() == Role.ADMIN) {
                authenticatedUser = usersAuthenticationService.loadUserByUsername(user.getEmail());
            } else {
                authenticatedUser = usersAuthenticationService.loadUserByPhoneNumber(user.getPhoneNo());
            }

            String jwtToken = jwtService.generateToken(authenticatedUser);
            String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

            JwtResponse jwtResponse = new JwtResponse()
                    .setToken(jwtToken)
                    .setExpiresIn(jwtService.getExpirationTime())
                    .setRefreshToken(refreshToken);

            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            String refreshToken = refreshTokenRequest.getRefreshToken();
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = usersAuthenticationService.loadUserByUsername(username);

            if (userDetails != null && jwtService.isRefreshTokenValid(refreshToken, userDetails)) {
                String newJwtToken = jwtService.generateToken(userDetails);
                String newRefreshToken = jwtService.generateRefreshToken(userDetails);

                JwtResponse jwtResponse = new JwtResponse()
                        .setToken(newJwtToken)
                        .setExpiresIn(jwtService.getExpirationTime())
                        .setRefreshToken(newRefreshToken);

                return ResponseEntity.ok(jwtResponse);
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(null);
        }
    }
}
