package com.backend.lms.service.impl;

import com.backend.lms.constants.JWTConstants;
import com.backend.lms.dto.auth.LoginRequestDto;
import com.backend.lms.dto.users.UserDto;
import com.backend.lms.service.IAuthService;
import com.backend.lms.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final IUserService iUserService;

    @Override
    public String generateJwtToken(String username) {
        String secret = env.getProperty(JWTConstants.JWT_SECRET_KEY, JWTConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("LMS")
                .setSubject("JWT Token")
                .claim("username", username)
                .claim("authorities", getAuthorities(username))
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public UserDto authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        );

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        if (authenticationResponse.isAuthenticated()) {
            String jwt = generateJwtToken(authenticationResponse.getName());

            UserDto userDTO;
            if (loginRequestDto.getUsername().contains("@")) {
                userDTO = iUserService.getUserByEmail(loginRequestDto.getUsername());
            } else {
                userDTO = iUserService.getUserByMobile(loginRequestDto.getUsername());
            }

            userDTO.setToken(jwt);
            userDTO.setRole(getAuthorities(loginRequestDto.getUsername()).stream().findFirst().orElse(""));

            return userDTO;
        }

        throw new RuntimeException("Authentication failed");
    }

    @Override
    public UserDto getCurrentUser(String token) {
        // Implement logic to extract user information from token and return UserDto
        // For example:
        String username = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(env.getProperty(JWTConstants.JWT_SECRET_KEY, JWTConstants.JWT_SECRET_DEFAULT_VALUE).getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token.replace(JWTConstants.JWT_HEADER, ""))
                .getBody()
                .getSubject();

        return iUserService.getUserByEmail(username); // Assuming email is used as identifier
    }

    private List<String> getAuthorities(String username) {
        // Get authorities from the user (if needed)
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, ""));
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
