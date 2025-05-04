package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RunningSubjectsActivity extends AppCompatActivity {
    
    private static final String TAG = "RunningSubjectsActivity";

    // Bottom navigation views
    private FrameLayout navHome, navCalendar, navAdd, navTasks, navMenu;
    // Subject cards
    private CardView cardMathematics, cardChemistry, cardLiterature, cardBiology, cardPhysics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_running_subjects);
            Log.d(TAG, "onCreate: Setting content view");
            
            initViews();
            setupBottomNavigation();
            setupSubjectCards();
            
            // Show a toast to confirm activity started
            Toast.makeText(this, "Running Subjects Loaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error creating RunningSubjectsActivity", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void initViews() {
        try {
            // Initialize navigation views
            navHome = findViewById(R.id.navHome);
            navCalendar = findViewById(R.id.navCalendar);
            navAdd = findViewById(R.id.navAdd);
            navTasks = findViewById(R.id.navTasks);
            navMenu = findViewById(R.id.navMenu);
            
            // Initialize subject cards
            cardMathematics = findViewById(R.id.cardMathematics);
            cardChemistry = findViewById(R.id.cardChemistry);
            cardLiterature = findViewById(R.id.cardLiterature);
            cardBiology = findViewById(R.id.cardBiology);
            cardPhysics = findViewById(R.id.cardPhysics);
            
//            // Setup debug button
//            Button btnDebug = findViewById(R.id.btnDebug);
//            btnDebug.setOnClickListener(v -> {
//                Toast.makeText(this, "Running Subjects Activity is working!", Toast.LENGTH_LONG).show();
//            });
            
            Log.d(TAG, "initViews: All views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void setupBottomNavigation() {
        try {
            // Home icon should navigate to ActivitiesActivity
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(RunningSubjectsActivity.this, ActivitiesActivity.class);
                startActivity(intent);
                finish();
            });
            
            // Calendar icon should navigate to DashboardActivity
            navCalendar.setOnClickListener(v -> {
                Intent intent = new Intent(RunningSubjectsActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            });
            
            // Add icon should open AddTaskActivity
            navAdd.setOnClickListener(v -> {
                Intent intent = new Intent(RunningSubjectsActivity.this, AddTaskActivity.class);
                startActivity(intent);
            });
            
            // Tasks icon should navigate to RewardsActivity
            navTasks.setOnClickListener(v -> {
                Intent intent = new Intent(RunningSubjectsActivity.this, RewardsActivity.class);
                startActivity(intent);
                finish();
            });
            
            // Menu (More options) icon - Navigate to ProfileActivity
            navMenu.setOnClickListener(v -> {
                Intent intent = new Intent(RunningSubjectsActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            });
            
            Log.d(TAG, "setupBottomNavigation: Navigation setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up bottom navigation", e);
            Toast.makeText(this, "Error setting up navigation: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void setupSubjectCards() {
        try {
            // Set click listeners for each subject card
            cardMathematics.setOnClickListener(v -> {
                // Handle Mathematics card click
                Toast.makeText(this, "Mathematics selected", Toast.LENGTH_SHORT).show();
            });
            
            cardChemistry.setOnClickListener(v -> {
                // Handle Chemistry card click
                Toast.makeText(this, "Chemistry selected", Toast.LENGTH_SHORT).show();
            });
            
            cardLiterature.setOnClickListener(v -> {
                // Handle Literature card click
                Toast.makeText(this, "Literature selected", Toast.LENGTH_SHORT).show();
            });
            
            cardBiology.setOnClickListener(v -> {
                // Handle Biology card click
                Toast.makeText(this, "Biology selected", Toast.LENGTH_SHORT).show();
            });
            
            cardPhysics.setOnClickListener(v -> {
                // Handle Physics card click
                Toast.makeText(this, "Physics selected", Toast.LENGTH_SHORT).show();
            });
            
            Log.d(TAG, "setupSubjectCards: Subject cards setup complete");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up subject cards", e);
            Toast.makeText(this, "Error setting up cards: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
} 