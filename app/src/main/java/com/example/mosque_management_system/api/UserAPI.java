package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.UserProfileResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAPI {

    @GET("/api/user/profile")
    Call<UserProfileResponse> getUserProfile();

    @Multipart
    @POST("/api/user/profile-image")
    Call<UserProfileResponse> uploadProfileImage(@Part MultipartBody.Part profileImage);
}
