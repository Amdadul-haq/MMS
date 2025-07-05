package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.CreateMosqueResponse;
import com.example.mosque_management_system.models.GenericResponse;
import com.example.mosque_management_system.models.JoinRequestBody;
import com.example.mosque_management_system.models.JoinRequestListResponse;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.models.MosqueListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MosqueAPI {

    @POST("api/mosques/create")
    Call<CreateMosqueResponse> createMosque(@Body Mosque mosque);

    // ✅ New: fetch all mosques
    @GET("api/mosques")
    Call<MosqueListResponse> getAllMosques();

    // ✅ New: send join request
    @POST("api/join-requests/send")
    Call<Void> sendJoinRequest(@Body JoinRequestBody requestBody);


    @GET("api/join-requests/pending")
    Call<JoinRequestListResponse> getPendingJoinRequests();

    @PUT("api/join-requests/approve/{requestId}")
    Call<GenericResponse> approveJoinRequest(@Path("requestId") String requestId);

    @PUT("api/join-requests/reject/{requestId}")
    Call<GenericResponse> rejectJoinRequest(@Path("requestId") String requestId);


}
