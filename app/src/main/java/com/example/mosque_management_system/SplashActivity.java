package com.example.mosque_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView welcomeText = findViewById(R.id.welcomeText);
        TextView taglineText = findViewById(R.id.taglineText);

        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fadeInUp = AnimationUtils.loadAnimation(this, R.anim.fade_in_up);
        welcomeText.startAnimation(zoomIn);
        taglineText.startAnimation(fadeInUp);

        // Delay and move to MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 4000);
    }
}
