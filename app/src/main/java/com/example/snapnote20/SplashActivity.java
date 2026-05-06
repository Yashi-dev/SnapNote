package com.example.snapnote20;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

// Splash screen shown when app starts
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply dark mode preference before showing UI
        SharedPrefHelper prefHelper = new SharedPrefHelper(this);
        if (prefHelper.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_splash);

        // After delay, check login status and navigate
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (prefHelper.isLoggedIn()) {
                // Already logged in -> go to HomeActivity
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                // Not logged in -> go to LoginActivity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish(); // Close splash so user can't go back to it
        }, SPLASH_DELAY);
    }
}
