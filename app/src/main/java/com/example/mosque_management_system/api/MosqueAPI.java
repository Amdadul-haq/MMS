package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.CreateMosqueResponse;
import com.example.mosque_management_system.models.JoinRequestBody;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.models.MosqueListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MosqueAPI {

    @POST("api/mosques/create")
    Call<CreateMosqueResponse> createMosque(@Body Mosque mosque);

    // ✅ New: fetch all mosques
    @GET("api/mosques")
    Call<MosqueListResponse> getAllMosques();

    // ✅ New: send join request
    @POST("api/join-requests/send")
    Call<Void> sendJoinRequest(@Body JoinRequestBody requestBody);
}
