package com.backend.lms.service;

import com.backend.lms.dto.inDto.LoginUserDto;
import com.backend.lms.dto.inDto.RegisterUserDto;
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

    public UsersAuthenticationService(UsersRepository userRepository, AuthenticationManager authenticationManager,
                                      PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public Users signup(RegisterUserDto input) {
        Users user = new Users();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPhoneNo(input.getPhoneNo());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(input.getRole());

        return userRepository.save(user);
    }

    public String authenticate(LoginUserDto input) {
        try {
            Users user;

            if (input.getUsername().contains("@")) {

                user = userRepository.findByEmail(input.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));
            } else {
                user = userRepository.findByPhoneNo(Long.parseLong(input.getUsername()))
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
            );

            UserDetails userDetails = loadUserByUsername(input.getUsername());
            return jwtService.generateToken(userDetails);
        } catch (Exception e) {
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
}
