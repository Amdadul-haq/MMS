package com.example.mosque_management_system.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private String fullName;

    @SerializedName("isAdmin") // ✅ Important for Gson to map JSON correctly
    private boolean isAdmin;

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

    public boolean isAdmin() {
        return isAdmin;
    }
}
