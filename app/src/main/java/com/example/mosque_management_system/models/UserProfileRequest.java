package com.example.mosque_management_system.models;

public class UserProfileRequest {
    private String fullName;
    private String profileImage;

    // Constructor
    public UserProfileRequest(String fullName, String profileImage) {
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    // Getter and Setter for fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for profileImage
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
