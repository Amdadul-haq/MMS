package com.example.mosque_management_system.admin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mosque_management_system.R;

public class AdminMoreFragment extends Fragment {

    public AdminMoreFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_more, container, false);

        // Example navigation to Profile, Settings, Logout, etc.
        LinearLayout profileLayout = view.findViewById(R.id.layoutProfile);
        LinearLayout settingsLayout = view.findViewById(R.id.layoutSettings);
        LinearLayout logoutLayout = view.findViewById(R.id.layoutLogout);

        profileLayout.setOnClickListener(v -> {
            // Handle profile navigation
        });

        settingsLayout.setOnClickListener(v -> {
            // Handle settings navigation
        });

        logoutLayout.setOnClickListener(v -> {
            // Handle logout
        });

        return view;
    }
}
