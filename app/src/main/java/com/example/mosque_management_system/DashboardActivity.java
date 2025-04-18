package com.example.mosque_management_system;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);

        // Set toolbar
        setSupportActionBar(toolbar);

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment()).commit();
            toolbar.setTitle("Home");
        }

        // Bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            updateFragment(item.getItemId());
            return true;
        });
    }

    private void updateFragment(int itemId) {
        Fragment selectedFragment = null;

        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_donate) {
            selectedFragment = new DonateFragment();
        } else if (itemId == R.id.nav_mosque) {
            selectedFragment = new MosqueFragment();
        } else if (itemId == R.id.nav_announcements) {
            selectedFragment = new AnnouncementsFragment();
        } else if (itemId == R.id.nav_more) {
            selectedFragment = new MoreFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            updateToolbar(itemId);
        }
    }

    private void updateToolbar(int itemId) {
        if (toolbar == null) return;

        if (itemId == R.id.nav_home) {
            toolbar.setTitle("Home");
        } else if (itemId == R.id.nav_donate) {
            toolbar.setTitle("Donate");
        } else if (itemId == R.id.nav_mosque) {
            toolbar.setTitle("Mosque Info");
        } else if (itemId == R.id.nav_announcements) {
            toolbar.setTitle("Announcements");
        } else if (itemId == R.id.nav_more) {
            toolbar.setTitle("More Options");
        }
    }
}
