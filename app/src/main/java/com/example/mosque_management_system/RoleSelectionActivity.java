package com.example.mosque_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    private Button btnAdmin, btnMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        btnAdmin = findViewById(R.id.btnAdmin);
        btnMember = findViewById(R.id.btnMember);

        btnAdmin.setOnClickListener(v -> navigateToLogin("admin"));
        btnMember.setOnClickListener(v -> navigateToLogin("member"));
    }

    private void navigateToLogin(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, MainActivity.class);
        intent.putExtra("role", role);
        startActivity(intent);
        finish(); // prevent back to splash
    }
}
