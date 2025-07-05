package com.example.mosque_management_system.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.repository.MosqueRepository;
import com.google.android.material.appbar.MaterialToolbar;

public class ImamInfoFragment extends Fragment {

    private TextView tvImamName, tvMosqueName, tvAddress, tvVillage, tvUpazila, tvZilla;
    private MosqueRepository mosqueRepository;
    private String mosqueId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imam_info, container, false);

        // Back arrow navigation
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // View bindings
        tvImamName = view.findViewById(R.id.tvImamName);
        tvMosqueName = view.findViewById(R.id.tvMosqueName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvVillage = view.findViewById(R.id.tvVillage);
        tvUpazila = view.findViewById(R.id.tvUpazila);
        tvZilla = view.findViewById(R.id.tvZilla);

        // Get mosqueId from arguments
        if (getArguments() != null) {
            mosqueId = getArguments().getString("mosqueId");
        }

        // Init repository with token
        String token = getActivity().getSharedPreferences("MyPrefs", 0).getString("jwt_token", null);
        mosqueRepository = new MosqueRepository(token);

        // Load data
        loadMosqueDetails();

        return view;
    }

    private void loadMosqueDetails() {
        if (mosqueId == null) {
            Toast.makeText(getContext(), "Mosque ID not provided", Toast.LENGTH_SHORT).show();
            return;
        }

        mosqueRepository.getMosqueById(mosqueId, new MosqueRepository.MosuqeCallback() {
            @Override
            public void onSuccess(Mosque mosque) {
                if (mosque != null) {
                    tvImamName.setText(mosque.getImamName() != null ? mosque.getImamName() : "N/A");
                    tvMosqueName.setText(mosque.getName() != null ? mosque.getName() : "N/A");
                    tvAddress.setText("Address: " + (mosque.getAddress() != null ? mosque.getAddress() : "N/A"));
                    tvVillage.setText("Village: " + (mosque.getVillage() != null ? mosque.getVillage() : "N/A"));
                    tvUpazila.setText("Upazila: " + (mosque.getUpazila() != null ? mosque.getUpazila() : "N/A"));
                    tvZilla.setText("Zilla: " + (mosque.getZilla() != null ? mosque.getZilla() : "N/A"));
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Failed to load mosque info: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
