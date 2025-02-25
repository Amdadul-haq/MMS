package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.DonationRequest;
import com.example.mosque_management_system.models.DonationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DonationAPI {
    @POST("api/donate")
    Call<DonationResponse> submitDonation(@Body DonationRequest donationRequest);
}
