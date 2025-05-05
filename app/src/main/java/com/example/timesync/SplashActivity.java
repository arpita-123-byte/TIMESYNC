package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds
    
    private ImageView logoImage;
    private TextView appNameText;
    private TextView taglineText;
    private ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize views
        logoImage = findViewById(R.id.splashLogo);
        appNameText = findViewById(R.id.appNameText);
        taglineText = findViewById(R.id.taglineText);
        loadingProgress = findViewById(R.id.loadingProgress);
        
        // Set initial alpha to 0 (invisible)
        logoImage.setAlpha(0f);
        appNameText.setAlpha(0f);
        taglineText.setAlpha(0f);
        loadingProgress.setAlpha(0f);
        
        // Start animations with slight delays for sequence effect
        startAnimations();
        
        // After SPLASH_DURATION, navigate to welcome screen
        new Handler().postDelayed(this::navigateToWelcomeScreen, SPLASH_DURATION);
    }
    
    private void startAnimations() {
        // Load animations
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        
        // Logo with special animation (zoom and rotate)
        logoImage.startAnimation(logoAnim);
        logoImage.setAlpha(1f);
        
        // Delay app name animation by 300ms
        new Handler().postDelayed(() -> {
            appNameText.startAnimation(fadeIn);
            appNameText.setAlpha(1f);
        }, 300);
        
        // Delay tagline animation by 600ms
        new Handler().postDelayed(() -> {
            taglineText.startAnimation(fadeIn);
            taglineText.setAlpha(1f);
        }, 600);
        
        // Delay progress bar animation by 1000ms
        new Handler().postDelayed(() -> {
            loadingProgress.startAnimation(fadeIn);
            loadingProgress.setAlpha(1f);
        }, 1000);
    }
    
    private void navigateToWelcomeScreen() {
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        startActivity(intent);
        
        // Apply transition animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        
        // Close the splash activity
        finish();
    }
} 