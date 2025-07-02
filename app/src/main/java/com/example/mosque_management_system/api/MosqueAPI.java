package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.CreateMosqueResponse;
import com.example.mosque_management_system.models.Mosque;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MosqueAPI {

    @POST("api/mosques/create")
    Call<CreateMosqueResponse> createMosque(@Body Mosque mosque);
}
