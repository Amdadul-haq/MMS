package com.example.mosque_management_system.admin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.mosque_management_system.R;
import com.example.mosque_management_system.admin.fragments.AdminHomeFragment;
import com.example.mosque_management_system.admin.fragments.RevenueExpenseFragment;
import com.example.mosque_management_system.admin.fragments.AnnouncementManagementFragment;
import com.example.mosque_management_system.admin.fragments.EventManagementFragment;
import com.example.mosque_management_system.admin.fragments.AdminMoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboardActivity extends AppCompatActivity {

    private BottomNavigationView adminBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        adminBottomNav = findViewById(R.id.adminBottomNav);

        // Load default fragment (Admin Home)
        if (savedInstanceState == null) {
            loadFragment(new AdminHomeFragment());
        }

//        adminBottomNav.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//
//            switch (item.getItemId()) {
//                case R.id.nav_admin_home:
//                    selectedFragment = new AdminHomeFragment();
//                    break;
//                case R.id.nav_finance:
//                    selectedFragment = new RevenueExpenseFragment();
//                    break;
//                case R.id.nav_announcements:
//                    selectedFragment = new AnnouncementManagementFragment();
//                    break;
//                case R.id.nav_events:
//                    selectedFragment = new EventManagementFragment();
//                    break;
//                case R.id.nav_more:
//                    selectedFragment = new AdminMoreFragment();
//                    break;
//            }
//
//            if (selectedFragment != null) {
//                loadFragment(selectedFragment);
//                return true;
//            }
//            return false;
//        });

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

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_fragment_container, fragment)
                .commit();
    }
}
