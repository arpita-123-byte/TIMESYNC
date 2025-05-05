package com.example.timesync;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppStatisticsActivity extends AppCompatActivity {
    private TextView tvDateFilter;
    private TextView tabCategories;
    private TextView tabGoals;
    private View tabIndicator;
    private ImageButton btnCalendar;
    private CardView cardDateFilter;
    
    private TextView tvSocialTime;
    private TextView tvSocialPercent;
    private ProgressBar progressSocial;
    
    private List<AppData> appDataList;
    
    private Calendar currentDate = Calendar.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_statistics);
        
        // Initialize views
        tvDateFilter = findViewById(R.id.tvDateFilter);
        tabCategories = findViewById(R.id.tabCategories);
        tabGoals = findViewById(R.id.tabGoals);
        tabIndicator = findViewById(R.id.tabIndicator);
        btnCalendar = findViewById(R.id.btnCalendar);
        cardDateFilter = findViewById(R.id.cardDateFilter);
        
        tvSocialTime = findViewById(R.id.tvSocialTime);
        tvSocialPercent = findViewById(R.id.tvSocialPercent);
        progressSocial = findViewById(R.id.progressSocial);
        
        // Initialize app data
        initAppData();
        
        // Set up tabs
        setupTabs();
        
        // Set up date filter
        cardDateFilter.setOnClickListener(v -> {
            showDateFilterOptions();
        });
        
        // Set up calendar button
        btnCalendar.setOnClickListener(v -> {
            // In a real implementation, this would show a date picker dialog
            showDatePickerDialog();
        });
        
        // Set up back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Navigate back to StatisticsActivity
            onBackPressed();
        });
        
        // Set up bottom navigation with visual feedback only
        setupBottomNavigation();
        
        // Update UI with initial data
        updateUIWithStatistics();
    }
    
    /**
     * Initialize app usage data
     */
    private void initAppData() {
        appDataList = new ArrayList<>();
        
        // Add mock app usage data based on the screenshot
        appDataList.add(new AppData("Spotify", "02h 30m", R.drawable.ic_spotify, 20));
        appDataList.add(new AppData("Twitter", "02h 30m", R.drawable.ic_twitter, 39));
        appDataList.add(new AppData("Youtube", "02h 30m", R.drawable.ic_youtube, 39));
        appDataList.add(new AppData("Whatsapp", "02h 30m", R.drawable.ic_whatsapp, 20));
        appDataList.add(new AppData("Instagram", "02h 30m", R.drawable.ic_instagram, 20));
    }
    
    /**
     * Set up tab selection behavior
     */
    private void setupTabs() {
        // Categories tab click
        tabCategories.setOnClickListener(v -> {
            selectTab(true);
        });
        
        // Goals tab click
        tabGoals.setOnClickListener(v -> {
            selectTab(false);
            
            // Navigate to GoalsActivity
            Intent intent = new Intent(this, GoalsActivity.class);
            startActivity(intent);
        });
    }
    
    /**
     * Select tab and update UI
     */
    private void selectTab(boolean isCategories) {
        // Update tab text colors
        tabCategories.setTextColor(isCategories ? Color.WHITE : Color.parseColor("#FF4B55"));
        tabGoals.setTextColor(isCategories ? Color.parseColor("#FF4B55") : Color.WHITE);
        
        // Keep bold for both tabs
        tabCategories.setTypeface(null, Typeface.BOLD);
        tabGoals.setTypeface(null, Typeface.BOLD);
        
        // Update indicator position
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tabIndicator.getLayoutParams();
        if (isCategories) {
            params.startToStart = R.id.tabCategories;
            params.endToEnd = R.id.tabCategories;
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            params.endToStart = ConstraintLayout.LayoutParams.UNSET;
        } else {
            params.startToStart = R.id.tabGoals;
            params.endToEnd = R.id.tabGoals;
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            params.endToStart = ConstraintLayout.LayoutParams.UNSET;
        }
        tabIndicator.setLayoutParams(params);
    }
    
    /**
     * Shows date filter options in a popup menu
     */
    private void showDateFilterOptions() {
        PopupMenu popup = new PopupMenu(this, cardDateFilter);
        popup.getMenuInflater().inflate(R.menu.date_filter_menu, popup.getMenu());
        
        // Set listener for option selection
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.filter_today) {
                tvDateFilter.setText("Today");
                currentDate = Calendar.getInstance();
            } else if (itemId == R.id.filter_yesterday) {
                tvDateFilter.setText("Yesterday");
                currentDate = Calendar.getInstance();
                currentDate.add(Calendar.DAY_OF_MONTH, -1);
            } else if (itemId == R.id.filter_this_week) {
                tvDateFilter.setText("This Week");
                currentDate = Calendar.getInstance();
                // Start of current week (Monday)
                currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            } else if (itemId == R.id.filter_last_week) {
                tvDateFilter.setText("Last Week");
                currentDate = Calendar.getInstance();
                // Start of last week
                currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                currentDate.add(Calendar.DAY_OF_MONTH, -7);
            } else if (itemId == R.id.filter_this_month) {
                tvDateFilter.setText("This Month");
                currentDate = Calendar.getInstance();
                currentDate.set(Calendar.DAY_OF_MONTH, 1);
            } else if (itemId == R.id.filter_custom) {
                // In a real app, this would show a date picker
                showDatePickerDialog();
                return true;
            }
            
            updateUIWithStatistics();
            return true;
        });
        
        popup.show();
    }
    
    /**
     * Shows date picker dialog
     * Note: In a real implementation, this would use a DatePickerDialog
     */
    private void showDatePickerDialog() {
        // This is a placeholder for a real date picker implementation
        // In a real app, you would show a DatePickerDialog and handle the result
        // For this example, we'll just set it to a fixed date
        tvDateFilter.setText("May 15");
    }
    
    /**
     * Update UI with statistics data
     */
    private void updateUIWithStatistics() {
        // Update social category time
        tvSocialTime.setText("09h 41m");
        
        // Update progress bar
        progressSocial.setProgress(49);
        
        // Update percentage text
        tvSocialPercent.setText("49%");
        
        // In a real implementation, you would update the CircularProgressView 
        // for each app based on the appDataList
    }
    
    /**
     * Inner class to represent app usage data
     */
    private static class AppData {
        String name;
        String usageTime;
        int iconRes;
        int productivity;
        
        AppData(String name, String usageTime, int iconRes, int productivity) {
            this.name = name;
            this.usageTime = usageTime;
            this.iconRes = iconRes;
            this.productivity = productivity;
        }
    }
    
    @Override
    public void onBackPressed() {
        // Navigate back to StatisticsActivity
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
    
    /**
     * Set up bottom navigation with visual feedback only (no navigation)
     */
    private void setupBottomNavigation() {
        // Get references to navigation icons
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navStats = findViewById(R.id.navStats);
        ImageView navAdd = findViewById(R.id.navAdd);
        ImageView navTasks = findViewById(R.id.navTasks);
        ImageView navProfile = findViewById(R.id.navProfile);
        
        // Home icon - navigate to ActivitiesActivity
        navHome.setOnClickListener(v -> {
            highlightNavIcon(navHome);
            Intent intent = new Intent(this, ActivitiesActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Stats icon - navigate to DashboardActivity
        navStats.setOnClickListener(v -> {
            highlightNavIcon(navStats);
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Add icon - navigate to MainActivity
        navAdd.setOnClickListener(v -> {
            highlightNavIcon(navAdd);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Tasks icon - navigate to Rewards
        navTasks.setOnClickListener(v -> {
            highlightNavIcon(navTasks);
            Intent intent = new Intent(this, RewardsActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Profile icon - navigate to profile
        navProfile.setOnClickListener(v -> {
            highlightNavIcon(navProfile);
            Intent intent = new Intent(AppStatisticsActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Highlight the first icon (Home) to match the image
        highlightNavIcon(navHome);
    }
    
    /**
     * Highlights the selected navigation icon and clears others
     */
    private void highlightNavIcon(ImageView selectedIcon) {
        // Remove highlight from all icons
        findViewById(R.id.navHome).setBackgroundResource(0);
        findViewById(R.id.navStats).setBackgroundResource(0);
        findViewById(R.id.navAdd).setBackgroundResource(0);
        findViewById(R.id.navTasks).setBackgroundResource(0);
        findViewById(R.id.navProfile).setBackgroundResource(0);
        
        // Update tint colors - reset all to default first
        ((ImageView)findViewById(R.id.navHome)).setColorFilter(null);
        ((ImageView)findViewById(R.id.navStats)).setColorFilter(null);
        ((ImageView)findViewById(R.id.navAdd)).setColorFilter(null);
        ((ImageView)findViewById(R.id.navTasks)).setColorFilter(null);
        ((ImageView)findViewById(R.id.navProfile)).setColorFilter(null);
        
        // Set selected icon tint to blue
        selectedIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_light));
        
        // Add animation effect
        ObjectAnimator.ofFloat(selectedIcon, "scaleX", 1f, 1.2f, 1f).setDuration(300).start();
        ObjectAnimator.ofFloat(selectedIcon, "scaleY", 1f, 1.2f, 1f).setDuration(300).start();
    }
} 