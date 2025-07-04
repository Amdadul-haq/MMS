package com.example.mosque_management_system.models;

public class JoinRequestBody {
    private String mosqueId;

    public JoinRequestBody(String mosqueId) {
        this.mosqueId = mosqueId;
    }

    public String getMosqueId() {
        return mosqueId;
    }

    public void setMosqueId(String mosqueId) {
        this.mosqueId = mosqueId;
    }
}
