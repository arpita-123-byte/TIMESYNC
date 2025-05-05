package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Button signUpButton;
    private Button signInButton;
    private ImageView logoImageView;
    private TextView welcomeTextView;
    private TextView descriptionTextView;
    private ImageView virtualAssistantImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize UI elements
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);
        logoImageView = findViewById(R.id.logoImageView);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        virtualAssistantImageView = findViewById(R.id.virtualAssistantImageView);
        
        // Start animations
        startAnimations();

        // Set click listeners
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to sign up screen
                navigateToSignUp();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to sign in screen
                navigateToSignIn();
            }
        });
    }
    
    private void startAnimations() {
        // Load animations
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        
        // Apply animations with delays for better sequence
        
        // Logo animation (zoom in)
        logoImageView.startAnimation(zoomIn);
        
        // Welcome text (fade in after 300ms)
        new Handler().postDelayed(() -> {
            welcomeTextView.setVisibility(View.VISIBLE);
            welcomeTextView.startAnimation(fadeIn);
        }, 300);
        
        // Description text (fade in after 500ms)
        new Handler().postDelayed(() -> {
            descriptionTextView.setVisibility(View.VISIBLE);
            descriptionTextView.startAnimation(fadeIn);
        }, 500);
        
        // Virtual assistant icon (bounce in after 700ms)
        new Handler().postDelayed(() -> {
            virtualAssistantImageView.setVisibility(View.VISIBLE);
            virtualAssistantImageView.startAnimation(bounce);
        }, 700);
        
        // Sign up & sign in buttons (slide up from bottom after 800ms)
        new Handler().postDelayed(() -> {
            signUpButton.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.VISIBLE);
            signUpButton.startAnimation(slideUp);
            signInButton.startAnimation(slideUp);
        }, 800);
    }

    /**
     * Navigate to the sign up screen
     */
    private void navigateToSignUp() {
        Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Navigate to the sign in screen
     */
    private void navigateToSignIn() {
        Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
} 