package com.example.mosque_management_system.models;

public class SignupRequest {
    private String fullName;
    private String email;
    private String password;

    public SignupRequest(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}
