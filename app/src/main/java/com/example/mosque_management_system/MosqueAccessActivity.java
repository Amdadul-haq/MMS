package com.example.mosque_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MosqueAccessActivity extends AppCompatActivity {

    Button btnCreateMosque, btnJoinMosque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosque_access);

        btnCreateMosque = findViewById(R.id.btnCreateMosque);
        btnJoinMosque = findViewById(R.id.btnJoinMosque);

//        btnCreateMosque.setOnClickListener(v -> {
//            Intent intent = new Intent(MosqueAccessActivity.this, CreateMosqueActivity.class);
//            startActivity(intent);
//        });
//
//        btnJoinMosque.setOnClickListener(v -> {
//            Intent intent = new Intent(MosqueAccessActivity.this, JoinMosqueActivity.class);
//            startActivity(intent);
//        });
    }
}
