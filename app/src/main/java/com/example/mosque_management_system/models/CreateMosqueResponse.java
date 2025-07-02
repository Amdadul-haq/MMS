package com.example.mosque_management_system.models;

import com.google.gson.annotations.SerializedName;

public class CreateMosqueResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("mosqueId")
    private String mosqueId;

    @SerializedName("mosqueCode")
    private String mosqueCode;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getMosqueId() {
        return mosqueId;
    }

    public String getMosqueCode() {
        return mosqueCode;
    }
}
