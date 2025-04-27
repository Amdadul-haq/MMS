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

import com.example.mosque_management_system.MainActivity;
import com.example.mosque_management_system.R;

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

        // 🚀 Set user name directly from SharedPreferences
        String fullName = sharedPreferences.getString("fullName", "Guest");
        userNameTextView.setText(fullName);

        // Set default profile image
        profileImageView.setImageResource(R.drawable.default_user);

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

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logoutUser());
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void logoutUser() {
        sharedPreferences.edit().clear().apply();

        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
