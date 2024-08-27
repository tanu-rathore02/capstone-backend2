package com.backend.lms.responses;

public class LoginResponse {
    private String token;
    private long expiresIn;

    // Getter and Setter for token
    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    // Getter and Setter for expiresIn
    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
