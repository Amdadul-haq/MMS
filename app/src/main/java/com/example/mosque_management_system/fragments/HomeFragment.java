package com.example.mosque_management_system.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.mosque_management_system.R;
import com.example.mosque_management_system.DashboardActivity;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private TextView tvClock, tvCountdown, tvGreeting, tvUserName, tvMosqueName, tvDailyHadith;
    private Handler handler = new Handler();
    private Runnable clockRunnable;
    private Date eidDate;
    private NavigationBarView bottomNavigationView;
    private CardView cardMosqueInfo, cardDonate, cardAnnouncements, cardEvents;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.APRIL, 10);
        eidDate = calendar.getTime();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        }

        // Start animations
        startAnimations();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        tvClock = view.findViewById(R.id.tvClock);
        tvCountdown = view.findViewById(R.id.tvCountdown);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvMosqueName = view.findViewById(R.id.tvMosqueName);
        tvDailyHadith = view.findViewById(R.id.tvDailyHadith);
        cardMosqueInfo = view.findViewById(R.id.cardMosqueInfo);
        cardDonate = view.findViewById(R.id.cardDonate);
        cardAnnouncements = view.findViewById(R.id.cardAnnouncements);
        cardEvents = view.findViewById(R.id.cardEvents);

        // Set initial values
        tvGreeting.setText("Assalamu Alaikum,");
        tvUserName.setText(getCurrentUserName());
        tvMosqueName.setText("Khiarpara Jame Moshjid");
        tvDailyHadith.setText("“The best among you are those who have the best manners and character.” – Bukhari");

        // Start clock updates
        startClockUpdates();
        updateCountdown();

        // Set click listeners with ripple animations
        cardMosqueInfo.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.button_click));
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.nav_mosque);
            }
        });

        cardDonate.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.button_click));
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.nav_donate);
            }
        });

        cardAnnouncements.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.button_click));
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.nav_announcements);
            }
        });

        cardEvents.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.button_click));
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.nav_more);//here I need to add the id of the events fragment
            }
        });

        // Hadith refresh
        tvDailyHadith.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.button_click));
            // In a real app, you would fetch a new hadith here
            tvDailyHadith.setText("“Whoever believes in Allah and the Last Day, let him speak good or remain silent.” – Bukhari");
        });

        return view;
    }

    private void startAnimations() {
        if (getView() != null) {
            getView().postDelayed(() -> {
                cardMosqueInfo.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                cardDonate.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                cardAnnouncements.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                cardEvents.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
            }, 300);
        }
    }

    private String getCurrentUserName() {
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            return prefs.getString("fullName", "User");
        }
        return "User";
    }

    private void startClockUpdates() {
        clockRunnable = new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null || tvClock == null) return;
                updateClock();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(clockRunnable);
    }

    private void updateClock() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        String hijriDate = "12 Ramadan 1446"; // Replace with actual Hijri date calculation
        tvClock.setText(String.format("%s | %s AH", currentTime, hijriDate));
    }

    private void updateCountdown() {
        if (tvCountdown == null) return;

        Date currentDate = new Date();
        long diffInMillis = eidDate.getTime() - currentDate.getTime();

        if (diffInMillis > 0) {
            long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
            tvCountdown.setText(String.format("%d days remaining", days));
        } else {
            tvCountdown.setText("Eid Mubarak!");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}