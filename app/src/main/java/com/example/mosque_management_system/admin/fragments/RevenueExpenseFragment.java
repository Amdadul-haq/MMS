package com.example.mosque_management_system.admin.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.api.DonationAPI;
import com.example.mosque_management_system.models.DonationSummaryResponse;
import com.example.mosque_management_system.network.RetrofitClient;
import com.example.mosque_management_system.utils.PreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RevenueExpenseFragment extends Fragment {

    private Spinner spinnerMonth;
    private TextView tvTotalDonation;

    private DonationAPI donationAPI;

    public RevenueExpenseFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revenue_expense, container, false);  // XML file name
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        tvTotalDonation = view.findViewById(R.id.tvTotalDonation);

        // Populate spinner with next 12 months
        List<String> monthList = getNextTwelveMonths();
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        String token = PreferenceHelper.getToken(requireContext());


        // Setup Retrofit with token
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(token);
        donationAPI = retrofit.create(DonationAPI.class);

        // Spinner onItemSelected
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = monthList.get(position);
                fetchMonthlyDonationSummary(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private List<String> getNextTwelveMonths() {
        List<String> monthList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        for (int i = 0; i < 12; i++) {
            monthList.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }

        return monthList;
    }

    private void fetchMonthlyDonationSummary(String month) {
        Call<DonationSummaryResponse> call = donationAPI.getMonthlySummary(month);
        call.enqueue(new Callback<DonationSummaryResponse>() {
            @Override
            public void onResponse(Call<DonationSummaryResponse> call, Response<DonationSummaryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double total = response.body().getTotalDonation();
                    tvTotalDonation.setText("৳ " + total);
                } else {
                    tvTotalDonation.setText("৳ 0");
                    Log.e("DonationSummary", "Error response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DonationSummaryResponse> call, Throwable t) {
                tvTotalDonation.setText("৳ 0");
                Log.e("DonationSummary", "API failed: " + t.getMessage());
            }
        });
    }
}
