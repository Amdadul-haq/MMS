package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.LoginRequest;
import com.example.mosque_management_system.models.LoginResponse;
import com.example.mosque_management_system.models.SignupRequest;
import com.example.mosque_management_system.models.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {

    @POST("api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("api/auth/signup")
    Call<SignupResponse> signupUser(@Body SignupRequest signupRequest);
}

