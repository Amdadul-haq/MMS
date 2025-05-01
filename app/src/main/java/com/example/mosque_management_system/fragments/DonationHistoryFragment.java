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
import com.example.mosque_management_system.models.PaginatedDonationResponse;
import com.example.mosque_management_system.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoDonations;
    private Spinner spinnerDonationType, spinnerDonationMonth, spinnerAmountFilter;

    private DonationHistoryAdapter adapter;
    private List<DonationRequest> fullDonationList = new ArrayList<>();

    private boolean isLoading = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 10;
    private boolean isLastPage = false;

    public DonationHistoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDonations);
        tvNoDonations = view.findViewById(R.id.tvNoDonations);
        spinnerDonationType = view.findViewById(R.id.spinnerDonationType);
        spinnerDonationMonth = view.findViewById(R.id.spinnerDonationMonth);
        spinnerAmountFilter = view.findViewById(R.id.spinnerAmountFilter);

        setupSpinners();
        setupRecyclerView();
        loadDonationHistory(currentPage);

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

        ArrayAdapter<CharSequence> amountAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.donation_amount_ranges, android.R.layout.simple_spinner_item);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmountFilter.setAdapter(amountAdapter);

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fullDonationList.clear();
                currentPage = 1;
                isLastPage = false;
                loadDonationHistory(currentPage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerDonationType.setOnItemSelectedListener(filterListener);
        spinnerDonationMonth.setOnItemSelectedListener(filterListener);
        spinnerAmountFilter.setOnItemSelectedListener(filterListener);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DonationHistoryAdapter(fullDonationList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                if (!isLoading && !isLastPage) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == fullDonationList.size() - 1) {
                        currentPage++;
                        loadDonationHistory(currentPage);
                    }
                }
            }
        });
    }

    private void loadDonationHistory(int page) {
        isLoading = true;

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Token not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        DonationAPI donationAPI = RetrofitClient.getRetrofitInstance(token).create(DonationAPI.class);
        Call<PaginatedDonationResponse> call = donationAPI.getDonationHistoryWithPagination(page, PAGE_SIZE);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PaginatedDonationResponse> call, Response<PaginatedDonationResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<DonationRequest> newDonations = response.body().getDonations();
                    if (newDonations.size() < PAGE_SIZE) {
                        isLastPage = true;
                        adapter.setShowFooter(true); // 👈 show footer instead of Toast
                    } else {
                        adapter.setShowFooter(false); // 👈 hide footer if more pages are expected
                    }
                    fullDonationList.addAll(newDonations);
                    applyFilter();
                } else {
                    Toast.makeText(getContext(), "Failed to load donation history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaginatedDonationResponse> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilter() {
        String selectedType = spinnerDonationType.getSelectedItem().toString();
        String selectedMonth = spinnerDonationMonth.getSelectedItem().toString();
        String selectedAmountRange = spinnerAmountFilter.getSelectedItem().toString();

        List<DonationRequest> filteredList = new ArrayList<>();

        for (DonationRequest donation : fullDonationList) {
            boolean matchesType = selectedType.equals("All") || donation.getDonationType().equals(selectedType);
            boolean matchesMonth = selectedMonth.equals("All") || donation.getDonationMonth().equals(selectedMonth);

            double amount;
            try {
                amount = Double.parseDouble(donation.getAmount());
            } catch (NumberFormatException e) {
                continue;
            }

            boolean matchesAmount = true;
            switch (selectedAmountRange) {
                case "Less than 50":
                    matchesAmount = amount < 50;
                    break;
                case "50 to 100":
                    matchesAmount = amount >= 50 && amount <= 100;
                    break;
                case "100 to 200":
                    matchesAmount = amount >= 100 && amount <= 200;
                    break;
                case "300 to 500":
                    matchesAmount = amount >= 300 && amount <= 500;
                    break;
                case "500 to 1000":
                    matchesAmount = amount >= 500 && amount <= 1000;
                    break;
                case "More than 1000":
                    matchesAmount = amount > 1000;
                    break;
            }

            if (matchesType && matchesMonth && matchesAmount) {
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

