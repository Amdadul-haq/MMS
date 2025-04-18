package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.DonationRequest;
import com.example.mosque_management_system.models.DonationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface DonationAPI {
    @POST("api/donate")
    Call<DonationResponse> submitDonation(@Body DonationRequest donationRequest);
    @GET("api/donations")
    Call<List<DonationRequest>> getDonationHistory();
}
