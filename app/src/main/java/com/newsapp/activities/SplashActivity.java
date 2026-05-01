package com.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.newsapp.R;
import com.newsapp.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManager sessionManager = new SessionManager(this);
        AppCompatDelegate.setDefaultNightMode(
                sessionManager.isDarkMode() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoIv = findViewById(R.id.iv_logo);
        TextView appNameTv = findViewById(R.id.tv_app_name);
        TextView taglineTv = findViewById(R.id.tv_tagline);

        // Animate elements
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        logoIv.startAnimation(fadeIn);
        appNameTv.startAnimation(fadeIn);
        taglineTv.startAnimation(fadeIn);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (sessionManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}
