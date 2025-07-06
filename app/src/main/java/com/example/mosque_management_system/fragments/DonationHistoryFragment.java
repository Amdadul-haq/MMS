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
import com.example.mosque_management_system.utils.PreferenceHelper;

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
                getContext(), R.array.donation_types_filter, android.R.layout.simple_spinner_item);
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
        String token = PreferenceHelper.getToken(requireContext());

        if (token == null) {
            Toast.makeText(getContext(), "Token not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedType = spinnerDonationType.getSelectedItem().toString();
        String selectedMonth = spinnerDonationMonth.getSelectedItem().toString();
        String selectedAmountRange = spinnerAmountFilter.getSelectedItem().toString();

        String donationType = selectedType.equals("All") ? null : selectedType;
        String donationMonth = selectedMonth.equals("All") ? null : selectedMonth;

        double minAmount = 0, maxAmount = 0;
        boolean useAmountFilter = true;

        switch (selectedAmountRange) {
            case "Less than 50":
                maxAmount = 50;
                break;
            case "50 to 100":
                minAmount = 50;
                maxAmount = 100;
                break;
            case "100 to 200":
                minAmount = 100;
                maxAmount = 200;
                break;
            case "300 to 500":
                minAmount = 300;
                maxAmount = 500;
                break;
            case "500 to 1000":
                minAmount = 500;
                maxAmount = 1000;
                break;
            case "More than 1000":
                minAmount = 1000;
                break;
            default:
                useAmountFilter = false;
                break;
        }
        String mosqueId = PreferenceHelper.getMosqueId(requireContext());

        if (mosqueId == null) {
            Toast.makeText(getContext(), "Mosque ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        DonationAPI donationAPI = RetrofitClient.getRetrofitInstance(token).create(DonationAPI.class);
        Call<PaginatedDonationResponse> call = donationAPI.getDonationHistoryWithFilters(
                page,
                PAGE_SIZE,
                donationMonth,
                donationType,
                useAmountFilter ? minAmount : null,
                useAmountFilter && maxAmount > 0 ? maxAmount : null,
                mosqueId

        );

        call.enqueue(new Callback<PaginatedDonationResponse>() {
            @Override
            public void onResponse(Call<PaginatedDonationResponse> call, Response<PaginatedDonationResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<DonationRequest> newDonations = response.body().getDonations();

                    if (page == 1) {
                        fullDonationList.clear(); // 🧹 Clear old data only for fresh load (new filters)
                    }

                    fullDonationList.addAll(newDonations);
                    adapter.updateList(fullDonationList); // 🔄 Notify adapter

                    if (newDonations.size() < PAGE_SIZE) {
                        isLastPage = true;
                        adapter.setShowFooter(true);
                    } else {
                        adapter.setShowFooter(false);
                    }

                    toggleEmptyState(); // ✅ Show/hide “No data” message
                }
                else {
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


    private void toggleEmptyState() {
        if (fullDonationList.isEmpty()) {
            tvNoDonations.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoDonations.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
