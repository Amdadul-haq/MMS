package com.example.mosque_management_system.models;

import com.example.mosque_management_system.models.JoinRequest;

import java.util.List;

public class JoinRequestListResponse {
    private boolean success;
    private List<JoinRequest> requests;

    public boolean isSuccess() { return success; }
    public List<JoinRequest> getRequests() { return requests; }
}
