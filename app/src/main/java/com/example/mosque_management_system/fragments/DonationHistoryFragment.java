package com.example.mosque_management_system.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.adapters.DonationHistoryAdapter;
import com.example.mosque_management_system.api.DonationAPI;
import com.example.mosque_management_system.models.DonationRequest;
import com.example.mosque_management_system.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoDonations;

    public DonationHistoryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDonations);
        tvNoDonations = view.findViewById(R.id.tvNoDonations);

        loadDonationHistory(); // 🔄 Fetch data when fragment loads

        return view;
    }

    private void loadDonationHistory() {
        // 🔐 Retrieve token from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Token not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Pass token into RetrofitClient
        DonationAPI donationAPI = RetrofitClient.getRetrofitInstance(token).create(DonationAPI.class);

        Call<List<DonationRequest>> call = donationAPI.getDonationHistory();

        call.enqueue(new Callback<List<DonationRequest>>() {
            @Override
            public void onResponse(Call<List<DonationRequest>> call, Response<List<DonationRequest>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DonationRequest> donationList = response.body();

                    if (donationList.isEmpty()) {
                        tvNoDonations.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvNoDonations.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        DonationHistoryAdapter adapter = new DonationHistoryAdapter(donationList);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load donation history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DonationRequest>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
