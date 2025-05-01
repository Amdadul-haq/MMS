package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.DonationRequest;
import com.example.mosque_management_system.models.DonationResponse;
import com.example.mosque_management_system.models.PaginatedDonationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DonationAPI {
    @POST("api/donate")
    Call<DonationResponse> submitDonation(@Body DonationRequest donationRequest);
    // Updated endpoint to support pagination
    @GET("api/donations")
    Call<PaginatedDonationResponse> getDonationHistoryWithPagination(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
