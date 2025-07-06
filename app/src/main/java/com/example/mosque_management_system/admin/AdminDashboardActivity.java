package com.example.mosque_management_system.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.admin.fragments.AdminHomeFragment;
import com.example.mosque_management_system.admin.fragments.RevenueExpenseFragment;
import com.example.mosque_management_system.admin.fragments.AnnouncementManagementFragment;
import com.example.mosque_management_system.admin.fragments.EventManagementFragment;
import com.example.mosque_management_system.admin.fragments.AdminMoreFragment;
import com.example.mosque_management_system.api.MosqueAPI;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.network.RetrofitClient;
import com.example.mosque_management_system.utils.PreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private BottomNavigationView adminBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        adminBottomNav = findViewById(R.id.adminBottomNav);

        // Fetch current admin's mosque info on start
        fetchMyMosque();

        // Load default fragment (Admin Home)
        if (savedInstanceState == null) {
            loadFragment(new AdminHomeFragment());
        }

        adminBottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_admin_home) {
                selectedFragment = new AdminHomeFragment();
            } else if (id == R.id.nav_finance) {
                selectedFragment = new RevenueExpenseFragment();
            } else if (id == R.id.nav_announcements) {
                selectedFragment = new AnnouncementManagementFragment();
            } else if (id == R.id.nav_events) {
                selectedFragment = new EventManagementFragment();
            } else if (id == R.id.nav_more) {
                selectedFragment = new AdminMoreFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });

        // Always show labels on bottom nav
        adminBottomNav.setLabelVisibilityMode(BottomNavigationView.LABEL_VISIBILITY_LABELED);
    }

    private void fetchMyMosque() {
        String token = PreferenceHelper.getToken(this);

        if (token == null) {
            Toast.makeText(this, "Token not found, please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        MosqueAPI mosqueAPI = RetrofitClient.getRetrofitInstance(token).create(MosqueAPI.class);
        Call<Mosque> call = mosqueAPI.getMyMosque();

        call.enqueue(new Callback<Mosque>() {
            @Override
            public void onResponse(Call<Mosque> call, Response<Mosque> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Mosque myMosque = response.body();
                    PreferenceHelper.saveMyMosqueDetails(AdminDashboardActivity.this, myMosque);
                    Log.d("AdminDashboard", "My mosque fetched: " + myMosque.getName());
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Failed to fetch mosque info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mosque> call, Throwable t) {
                Toast.makeText(AdminDashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_fragment_container, fragment)
                .commit();
    }
}
