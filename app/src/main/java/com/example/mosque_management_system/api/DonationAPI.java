package com.example.mosque_management_system.api;

import com.example.mosque_management_system.models.DonationRequest;
import com.example.mosque_management_system.models.DonationResponse;
import com.example.mosque_management_system.models.DonationSummaryResponse;
import com.example.mosque_management_system.models.PaginatedDonationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DonationAPI {
    @POST("/api/donate")
    Call<DonationResponse> submitDonation(@Body DonationRequest donationRequest);
    // Updated endpoint to support pagination
    @GET("api/donations")
    Call<PaginatedDonationResponse> getDonationHistoryWithFilters(
            @Query("page") int page,
            @Query("size") int size,
            @Query("month") String month,
            @Query("type") String type,
            @Query("minAmount") Double minAmount,
            @Query("maxAmount") Double maxAmount,
            @Query("mosqueId") String mosqueId   // ✅ ADD HERE
    );


    @GET("api/donations/summary")
    Call<DonationSummaryResponse> getMonthlySummary(@Query("month") String month);

}
