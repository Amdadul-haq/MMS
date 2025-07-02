package com.example.mosque_management_system;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mosque_management_system.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Default fragment: LoginFragment
        if (savedInstanceState == null) {
            loadFragment(new LoginFragment());
        }
    }

    // Fragment Loader Function
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    public void navigateToDashboard() {
        Intent intent = new Intent(MainActivity.this, MosqueAccessActivity.class);
        startActivity(intent);
        finish(); // Finish MainActivity so user can't go back to login page
    }
}
