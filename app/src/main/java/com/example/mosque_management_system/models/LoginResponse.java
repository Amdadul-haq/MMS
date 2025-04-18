package com.example.mosque_management_system.models;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private String fullName; // ✅ Add this

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
    public String getFullName() {
        return fullName;
    }
}
