package com.backend.lms.service;

import com.backend.lms.dto.LoginUserDto;
import com.backend.lms.dto.RegisterUserDto;
import com.backend.lms.model.Users;
import com.backend.lms.repository.UsersRepository;
import com.backend.lms.securityConfig.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersAuthenticationService implements UserDetailsService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UsersAuthenticationService(
            UsersRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Signup method updated to handle role
    public Users signup(RegisterUserDto input) {
        Users user = new Users();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPhoneNo(input.getPhoneNo());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(input.getRole());

        return userRepository.save(user);
    }

    // Authentication method updated to handle login via email or phone number based on role
    public Users authenticate(LoginUserDto input) {
        try {
            Users user;
            System.out.println("Username: " + input.getUsername());

            if (input.getUsername().contains("@")) {
                user = userRepository.findByEmail(input.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));
            } else {
                user = userRepository.findByPhoneNo(Long.parseLong(input.getUsername()))
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }

            // Add debug log before authentication
            System.out.println("Authenticating user: " + input.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );

            // Add debug log after authentication
            System.out.println("Authentication successful for user: " + user.getEmail());
            System.out.println("for user" + user.getPhoneNo());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.contains("@")) {
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        } else {
            return userRepository.findByPhoneNo(Long.parseLong(username))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + username));
        }
    }

    public UserDetails loadUserByPhoneNumber(Long phoneNo) throws UsernameNotFoundException {
        return userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNo));
    }

    // New methods for refresh tokens
    public String generateRefreshToken(UserDetails userDetails) {
        return jwtService.generateRefreshToken(userDetails);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return jwtService.isRefreshTokenValid(token, userDetails);
    }
}
