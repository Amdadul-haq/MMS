package com.example.mosque_management_system.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.mosque_management_system.MainActivity;
import com.example.mosque_management_system.R;
import com.example.mosque_management_system.api.AuthAPI;
import com.example.mosque_management_system.api.UserAPI;
import com.example.mosque_management_system.models.UserProfileResponse;
import com.example.mosque_management_system.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private String token;
    private TextView userNameTextView;
    private ImageView profileImageView;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("jwt_token", null);

        userNameTextView = view.findViewById(R.id.user_name);
        profileImageView = view.findViewById(R.id.profile_image);


        fetchUserProfile();
        profileImageView.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new UpdateProfileFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });


        TextView navProfile = view.findViewById(R.id.nav_profile);
        TextView navSettings = view.findViewById(R.id.nav_settings);
        TextView navDonate = view.findViewById(R.id.nav_donate);
        TextView navDonationHistory = view.findViewById(R.id.nav_donation_history);
        TextView navLogout = view.findViewById(R.id.nav_logout);

        View.OnClickListener menuClickListener = v -> {
            Fragment selectedFragment = null;
            int id = v.getId();

            if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (id == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            } else if (id == R.id.nav_donate) {
                selectedFragment = new DonateFragment();
            } else if (id == R.id.nav_donation_history) {
                selectedFragment = new DonationHistoryFragment();
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };

        navProfile.setOnClickListener(menuClickListener);
        navSettings.setOnClickListener(menuClickListener);
        navDonate.setOnClickListener(menuClickListener);
        navDonationHistory.setOnClickListener(menuClickListener);
        navLogout.setOnClickListener(v -> showLogoutDialog());

        return view;
    }

    private void fetchUserProfile() {
        if (token == null) return;

        UserAPI userAPI = RetrofitClient.getRetrofitInstance(token).create(UserAPI.class);
        Call<UserProfileResponse> call = userAPI.getUserProfile();

        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userNameTextView.setText(response.body().getFullName());

                    String imageUrl = "https://mosquemanagementsystem.onrender.com/uploads/" + response.body().getProfileImage();
                    Glide.with(requireContext()).load(imageUrl).placeholder(R.drawable.default_user).into(profileImageView);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load profile: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logoutUser());
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void logoutUser() {
        if (token == null) {
            Toast.makeText(getContext(), "Token not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthAPI authAPI = RetrofitClient.getRetrofitInstance(token).create(AuthAPI.class);
        Call<Void> call = authAPI.logoutUser();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    sharedPreferences.edit().clear().apply();

                    Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Logout failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
