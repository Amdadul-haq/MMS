package com.example.mosque_management_system.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MosqueListResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("mosques")
    private List<Mosque> mosques;

    public boolean isSuccess() {
        return success;
    }

    public List<Mosque> getMosques() {
        return mosques;
    }
}
