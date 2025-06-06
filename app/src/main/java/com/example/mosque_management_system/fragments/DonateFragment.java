package com.example.mosque_management_system.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.api.DonationAPI;
import com.example.mosque_management_system.models.DonationRequest;
import com.example.mosque_management_system.models.DonationResponse;
import com.example.mosque_management_system.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonateFragment extends Fragment {

    private EditText etDonorName, etAmount;
    private Spinner spinnerDonationType, spinnerDonationMonth;
    private RadioGroup radioGroupPayment;
    private Button btnContinue;
    private DonationAPI donationAPI;

    public DonateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        etDonorName = view.findViewById(R.id.etDonorName);
        etAmount = view.findViewById(R.id.etAmount);
        spinnerDonationType = view.findViewById(R.id.spinnerDonationType);
        spinnerDonationMonth = view.findViewById(R.id.spinnerDonationMonth);
        radioGroupPayment = view.findViewById(R.id.radioGroupPayment);
        btnContinue = view.findViewById(R.id.btnContinue);

        // Populate Donation Month Spinner dynamically
        List<String> monthList = getNextTwelveMonths();
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDonationMonth.setAdapter(monthAdapter);

        // Get token and name from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);
        String fullName = prefs.getString("fullName", "");

        // Pre-fill donor name
        etDonorName.setText(fullName);

        // Initialize Retrofit API
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(token);
        donationAPI = retrofit.create(DonationAPI.class);

        // Continue Button Click
        btnContinue.setOnClickListener(v -> handleDonation());
    }

    private List<String> getNextTwelveMonths() {
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        for (int i = 0; i < 12; i++) {
            months.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }

        return months;
    }

    private void handleDonation() {
        String donorName = etDonorName.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String donationType = spinnerDonationType.getSelectedItem().toString();
        String donationMonth = spinnerDonationMonth.getSelectedItem().toString();

        if (donorName.isEmpty() || amount.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedPaymentId == -1) {
            Toast.makeText(getActivity(), "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedPaymentButton = getView().findViewById(selectedPaymentId);
        String paymentMethod = selectedPaymentButton.getText().toString();

        if (paymentMethod.equalsIgnoreCase("By Hand")) {
            showConfirmationDialog(donorName, amount, donationType, donationMonth);
        } else {
            Toast.makeText(getActivity(), paymentMethod + " will be available soon", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(String donorName, String amount, String donationType, String donationMonth) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Donation")
                .setMessage("Donor: " + donorName + "\nAmount: " + amount + " BDT\nType: " + donationType + "\nMonth: " + donationMonth + "\n\nAre you sure you want to donate?")
                .setPositiveButton("Yes", (dialog, which) ->
                        submitDonation(donorName, amount, donationType, donationMonth, "By Hand"))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void submitDonation(String donorName, String amount, String donationType, String donationMonth, String paymentMethod) {
        DonationRequest donationRequest = new DonationRequest(donorName, donationType, donationMonth, amount, paymentMethod);
        Call<DonationResponse> call = donationAPI.submitDonation(donationRequest);

        call.enqueue(new Callback<DonationResponse>() {
            @Override
            public void onResponse(Call<DonationResponse> call, Response<DonationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showSuccessDialog();
                } else {
                    Toast.makeText(getActivity(), "Failed to submit donation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonationResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_success, null);
        AlertDialog successDialog = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button btnDone = dialogView.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> {
            successDialog.dismiss();
            clearFormFields();
        });

        successDialog.show();
    }

    private void clearFormFields() {
        etAmount.setText("");
        radioGroupPayment.clearCheck();
        spinnerDonationType.setSelection(0);
        spinnerDonationMonth.setSelection(0);
    }
}
