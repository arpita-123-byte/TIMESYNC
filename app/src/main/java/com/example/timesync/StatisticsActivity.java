package com.example.timesync;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {
    private TextView tvTotalTime;
    private TextView tvProductiveTime;
    private TextView tvUnproductiveTime;
    private TextView tvProductivityScore;
    private TextView tvProductivityPercent;
    private TextView tvDateFilter;
    private TextView tabOverview;
    private TextView tabCategories;
    private View tabIndicator;
    private CardView cardDateFilter;
    private ImageButton btnCalendar;
    private CircularProgressView productivityGauge;
    
    // Bottom navigation
    private ImageView navHome;
    private ImageView navStats;
    private ImageView navAdd;
    private ImageView navGoals;
    private ImageView navProfile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        
        // Initialize views
        initializeViews();
        
        // Set up tabs
        setupTabs();
        
        // Set up date filter
        setupDateFilter();
        
        // Set up bottom navigation
        setupBottomNavigation();
        
        // Update UI with statistics
        updateStatistics();
    }
    
    private void initializeViews() {
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvProductiveTime = findViewById(R.id.tvProductiveTime);
        tvUnproductiveTime = findViewById(R.id.tvUnproductiveTime);
        tvProductivityScore = findViewById(R.id.tvProductivityScore);
        tvProductivityPercent = findViewById(R.id.tvProductivityPercent);
        tvDateFilter = findViewById(R.id.tvDateFilter);
        tabOverview = findViewById(R.id.tabOverview);
        tabCategories = findViewById(R.id.tabCategories);
        tabIndicator = findViewById(R.id.tabIndicator);
        cardDateFilter = findViewById(R.id.cardDateFilter);
        btnCalendar = findViewById(R.id.btnCalendar);
        productivityGauge = findViewById(R.id.productivityGauge);
        
        // Bottom navigation
        navHome = findViewById(R.id.navHome);
        navStats = findViewById(R.id.navStats);
        navAdd = findViewById(R.id.navAdd);
        navGoals = findViewById(R.id.navGoals);
        navProfile = findViewById(R.id.navProfile);
    }
    
    private void setupTabs() {
        // Overview tab click (already selected)
        tabOverview.setOnClickListener(v -> {
            selectTab(true);
        });
        
        // Categories tab click
        tabCategories.setOnClickListener(v -> {
            selectTab(false);
            // Navigate to categories
            Intent intent = new Intent(this, AppStatisticsActivity.class);
            startActivity(intent);
        });
    }
    
    private void selectTab(boolean isOverview) {
        // Update tab text colors
        tabOverview.setTextColor(isOverview ? Color.WHITE : Color.parseColor("#A0A0A0"));
        tabCategories.setTextColor(isOverview ? Color.parseColor("#A0A0A0") : Color.WHITE);
        
        // Update tab styles
        tabOverview.setTypeface(null, isOverview ? Typeface.BOLD : Typeface.NORMAL);
        tabCategories.setTypeface(null, isOverview ? Typeface.NORMAL : Typeface.BOLD);
        
        // Move indicator
        tabIndicator.animate()
                .x(isOverview ? tabOverview.getX() : tabCategories.getX())
                .setDuration(200)
                .start();
    }
    
    private void setupDateFilter() {
        // Set up date filter dropdown
        cardDateFilter.setOnClickListener(v -> {
            showDateFilterOptions();
        });
        
        // Set up calendar button
        btnCalendar.setOnClickListener(v -> {
            // Show calendar picker
            showDatePicker();
        });
    }
    
    private void showDateFilterOptions() {
        PopupMenu popup = new PopupMenu(this, cardDateFilter);
        popup.getMenuInflater().inflate(R.menu.date_filter_menu, popup.getMenu());
        
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.filter_today) {
                tvDateFilter.setText("Today");
            } else if (itemId == R.id.filter_yesterday) {
                tvDateFilter.setText("Yesterday");
            } else if (itemId == R.id.filter_this_week) {
                tvDateFilter.setText("This Week");
            } else if (itemId == R.id.filter_last_week) {
                tvDateFilter.setText("Last Week");
            } else if (itemId == R.id.filter_this_month) {
                tvDateFilter.setText("This Month");
            } else {
                return false;
            }
            
            updateStatistics();
            return true;
        });
        
        popup.show();
    }
    
    private void showDatePicker() {
        // In a real app, show a date picker dialog
        // For now, just toggle between dates
        if (tvDateFilter.getText().equals("Today")) {
            tvDateFilter.setText("Yesterday");
        } else {
            tvDateFilter.setText("Today");
        }
        updateStatistics();
    }
    
    private void updateStatistics() {
        // This would normally fetch data from a repository
        // For now, set fixed values to match the UI design
        
        // Set total time
        tvTotalTime.setText("09h 41m");
        
        // Set productive time
        tvProductiveTime.setText("05h 32m");
        
        // Set unproductive time
        tvUnproductiveTime.setText("03h 09m");
        
        // Set productivity score
        tvProductivityScore.setText("56.9");
        
        // Set productivity percentage change
        tvProductivityPercent.setText("↑ 4.5%");
        
        // Set progress on the productivity gauge (0-100)
        if (productivityGauge != null) {
            productivityGauge.setProgress(56.9f);
        }
    }
    
    private void setupBottomNavigation() {
        // Home icon - navigate to Activities
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivitiesActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Stats icon - navigate to Dashboard
        navStats.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Add icon - already on StatisticsActivity
        navAdd.setOnClickListener(v -> {
            // Already on this screen, just provide visual feedback
            highlightNavIcon(navAdd);
        });
        
        // Goals icon - navigate to Rewards
        navGoals.setOnClickListener(v -> {
            Intent intent = new Intent(this, RewardsActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Profile icon - navigate to Profile
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Highlight the Add icon by default
        highlightNavIcon(navAdd);
    }
    
    private void highlightNavIcon(ImageView selectedIcon) {
        // Reset tints
        navHome.setColorFilter(null);
        navStats.setColorFilter(null);
        navAdd.setColorFilter(null);
        navGoals.setColorFilter(null);
        navProfile.setColorFilter(null);
        
        // Set selected icon tint
        selectedIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_light));
        
        // Add subtle animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(selectedIcon, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(selectedIcon, "scaleY", 1f, 1.2f, 1f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);
        scaleX.start();
        scaleY.start();
    }
    
    @Override
    public void onBackPressed() {
        // Navigate to ActivitiesActivity instead of standard back behavior
        Intent intent = new Intent(this, ActivitiesActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
} 