package com.example.mosque_management_system.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoDonations;
    private Spinner spinnerDonationType, spinnerDonationMonth;

    private DonationHistoryAdapter adapter;
    private List<DonationRequest> fullDonationList = new ArrayList<>();

    public DonationHistoryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDonations);
        tvNoDonations = view.findViewById(R.id.tvNoDonations);
        spinnerDonationType = view.findViewById(R.id.spinnerDonationType);
        spinnerDonationMonth = view.findViewById(R.id.spinnerDonationMonth);

        setupSpinners(); // 🌀 Filter setup
        loadDonationHistory(); // 🔄 Load data

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.donation_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDonationType.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.donation_months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDonationMonth.setAdapter(monthAdapter);

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterDonations();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };

        spinnerDonationType.setOnItemSelectedListener(filterListener);
        spinnerDonationMonth.setOnItemSelectedListener(filterListener);
    }

    private void loadDonationHistory() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Token not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        DonationAPI donationAPI = RetrofitClient.getRetrofitInstance(token).create(DonationAPI.class);
        Call<List<DonationRequest>> call = donationAPI.getDonationHistory();

        call.enqueue(new Callback<List<DonationRequest>>() {
            @Override
            public void onResponse(Call<List<DonationRequest>> call, Response<List<DonationRequest>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullDonationList = response.body();
                    if (fullDonationList.isEmpty()) {
                        tvNoDonations.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvNoDonations.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter = new DonationHistoryAdapter(fullDonationList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);

                        filterDonations(); // Apply default filter
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

    private void filterDonations() {
        if (adapter == null) return; // prevent NullPointerException
        String selectedType = spinnerDonationType.getSelectedItem().toString();
        String selectedMonth = spinnerDonationMonth.getSelectedItem().toString();

        List<DonationRequest> filteredList = new ArrayList<>();
        for (DonationRequest donation : fullDonationList) {
            boolean matchesType =selectedType.equals("All") || donation.getDonationType().equals(selectedType);
            boolean matchesMonth =selectedMonth.equals("All") || donation.getDonationMonth().equals(selectedMonth);
            if (matchesType && matchesMonth) {
                filteredList.add(donation);
            }
        }

        adapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            tvNoDonations.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoDonations.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
