package com.example.mosque_management_system.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mosque_management_system.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MosqueFragment extends Fragment {

    public MosqueFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mosque, container, false);

        // Find views
        MaterialCardView mosqueInfoCard = view.findViewById(R.id.mosque_info_card);
        MaterialButton btnImamInfo = view.findViewById(R.id.btn_imam_info);
        MaterialButton btnGallery = view.findViewById(R.id.btn_gallery);
        MaterialButton btnContact = view.findViewById(R.id.btn_contact);

        // Card animation
        mosqueInfoCard.setAlpha(0f);
        mosqueInfoCard.setTranslationY(100f);
        mosqueInfoCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        // Button animations with staggered effect
        View[] buttons = {btnImamInfo, btnGallery, btnContact};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setAlpha(0f);
            buttons[i].setTranslationY(50f);
            buttons[i].animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .setStartDelay(200 + (i * 100))
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }

        return view;
    }
}