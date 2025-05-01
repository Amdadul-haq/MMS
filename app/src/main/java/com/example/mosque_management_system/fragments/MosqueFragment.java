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
        MaterialCardView[] featureCards = {cardImamInfo, cardGallery, cardContact,cardHistory};
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

        // TODO: Set onClickListeners if needed
        cardImamInfo.setOnClickListener(v -> {
            // Navigate to Imam Info screen
        });

        cardGallery.setOnClickListener(v -> {
            // Navigate to Gallery screen
        });

        cardContact.setOnClickListener(v -> {
            // Show Contact information
        });
        cardHistory.setOnClickListener(v ->{
            //show History information
        });

        return view;
    }
}
