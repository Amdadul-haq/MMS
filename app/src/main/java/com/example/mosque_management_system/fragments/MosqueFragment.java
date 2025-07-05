package com.example.mosque_management_system.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.models.Mosque;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

public class MosqueFragment extends Fragment {

    private Mosque currentMosque;

    public MosqueFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mosque, container, false);

        // Load mosque info from SharedPreferences
        loadMosqueFromPrefs();

        // Find views
        MaterialCardView mosqueInfoCard = view.findViewById(R.id.mosque_info_card);
        MaterialCardView cardImamInfo = view.findViewById(R.id.card_imam_info);
        MaterialCardView cardGallery = view.findViewById(R.id.card_gallery);
        MaterialCardView cardContact = view.findViewById(R.id.card_contact);
        MaterialCardView cardHistory = view.findViewById(R.id.card_history);

        // Animate mosque info card
        mosqueInfoCard.setAlpha(0f);
        mosqueInfoCard.setTranslationY(100f);
        mosqueInfoCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        // Animate feature cards with staggered effect
        MaterialCardView[] featureCards = {cardImamInfo, cardGallery, cardContact, cardHistory};
        for (int i = 0; i < featureCards.length; i++) {
            MaterialCardView card = featureCards[i];
            card.setAlpha(0f);
            card.setTranslationY(60f);
            card.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(300 + (i * 150))
                    .setDuration(500)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }

        // Update mosque info card texts dynamically if needed
        // For example, you can set mosque name & village here:
        // TextView tvMosqueName = mosqueInfoCard.findViewById(R.id.tvMosqueName);
        // tvMosqueName.setText(currentMosque != null ? currentMosque.getName() : "No Mosque Selected");

        // Imam Info Card click
        cardImamInfo.setOnClickListener(v -> {
            String mosqueId = requireContext()
                    .getSharedPreferences("MosquePrefs", 0)
                    .getString("mosqueId", null);

            if (mosqueId != null) {
                ImamInfoFragment fragment = new ImamInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mosqueId", mosqueId);
                fragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Mosque ID not found", Toast.LENGTH_SHORT).show();
            }
        });


        // Gallery Card click
        cardGallery.setOnClickListener(v -> {
            // TODO: Navigate to GalleryFragment or activity
        });

        // Contact Card click
        cardContact.setOnClickListener(v -> {
            // TODO: Show Contact info or navigate
        });

        // History Card click
        cardHistory.setOnClickListener(v -> {
            if (currentMosque != null) {
                // For example, open a fragment or dialog showing mosque's name, village, upazila, zilla
                // TODO: Implement HistoryFragment and pass mosque info as needed
            }
        });

        return view;
    }

    private void loadMosqueFromPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MosquePrefs", Context.MODE_PRIVATE);
        String mosqueJson = prefs.getString("mosqueJson", null);
        if (mosqueJson != null) {
            currentMosque = new Gson().fromJson(mosqueJson, Mosque.class);
        }
    }
}
