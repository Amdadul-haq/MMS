package com.example.mosque_management_system.models;

public class JoinRequest {
    private String _id;
    private User userId;
    private Mosque mosqueId;
    private String status;

    // Nested models
    public static class User {
        private String fullName;
        private String email;

        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
    }

    public static class Mosque {
        private String name;

        public String getName() { return name; }
    }

    // Getters
    public String getId() { return _id; }
    public User getUserId() { return userId; }
    public Mosque getMosqueId() { return mosqueId; }
    public String getStatus() { return status; }
}
