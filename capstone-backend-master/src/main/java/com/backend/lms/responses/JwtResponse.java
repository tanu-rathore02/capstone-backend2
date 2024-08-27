package com.backend.lms.responses;

public class JwtResponse {
    private String token;
    private String refreshToken;
    private long expiresIn;

    // Getters and setters

    public String getToken() {
        return token;
    }

    public JwtResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public JwtResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public JwtResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
