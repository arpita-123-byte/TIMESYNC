package com.example.timesync;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {
    private TextView tvProductiveTime;
    private TextView tvUnproductiveTime;
    private TextView tvDateFilter;
    private TextView tvMonth;
    private TextView tabOverview;
    private TextView tabCategories;
    private View tabIndicator;
    private CalendarView calendarView;
    private CardView cardDateFilter;
    private ImageButton btnCalendar;
    private ImageButton btnPrevMonth;
    private ImageButton btnNextMonth;
    private LinearLayout statsContainer;
    private LinearLayout calendarContainer;
    
    private Calendar currentDate = Calendar.getInstance();
    private String currentDateFilter = "May 15";
    
    private StatsRepository statsRepository;
    private boolean isCalendarVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        
        // Initialize repository
        statsRepository = StatsRepository.getInstance();
        
        // Initialize views
        tvProductiveTime = findViewById(R.id.tvProductiveTime);
        tvUnproductiveTime = findViewById(R.id.tvUnproductiveTime);
        tvDateFilter = findViewById(R.id.tvDateFilter);
        tvMonth = findViewById(R.id.tvMonth);
        tabOverview = findViewById(R.id.tabOverview);
        tabCategories = findViewById(R.id.tabCategories);
        tabIndicator = findViewById(R.id.tabIndicator);
        calendarView = findViewById(R.id.calendarView);
        cardDateFilter = findViewById(R.id.cardDateFilter);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        statsContainer = findViewById(R.id.statsContainer);
        calendarContainer = findViewById(R.id.calendarContainer);
        
        // Set up calendar
        tvMonth.setText(calendarView.getCurrentMonthName());
        
        // Initialize with today's date selected
        calendarView.setSelectedDate(Calendar.getInstance());
        
        // Initially hide the calendar
        calendarContainer.setVisibility(View.GONE);
        
        // Set up tabs
        setupTabs();
        
        // Set up previous month button
        btnPrevMonth.setOnClickListener(v -> {
            calendarView.previousMonth();
            tvMonth.setText(calendarView.getCurrentMonthName());
        });
        
        // Set up next month button
        btnNextMonth.setOnClickListener(v -> {
            calendarView.nextMonth();
            tvMonth.setText(calendarView.getCurrentMonthName());
        });
        
        // Set up custom bottom navigation
        setupBottomNavigation();
        
        // Set up date filter dropdown
        cardDateFilter.setOnClickListener(v -> {
            showDateFilterOptions();
        });
        
        // Set up calendar button to toggle calendar visibility
        btnCalendar.setOnClickListener(v -> {
            toggleCalendarVisibility();
        });
        
        // Set up calendar date selection
        calendarView.setOnDateSelectedListener(selectedDate -> {
            currentDate = selectedDate;
            
            // Format the date as "Month Day" (e.g., "May 15")
            currentDateFilter = String.format(Locale.getDefault(), "%tB %td", 
                    selectedDate, selectedDate);
            
            tvDateFilter.setText(currentDateFilter);
            updateStats();
        });
        
        // Load initial stats
        updateStats();
    }
    
    /**
     * Set up tab selection behavior
     */
    private void setupTabs() {
        // Overview tab click
        tabOverview.setOnClickListener(v -> {
            selectTab(true);
        });
        
        // Categories tab click
        tabCategories.setOnClickListener(v -> {
            selectTab(false);
            
            // Launch the new App Statistics screen
            Intent intent = new Intent(this, AppStatisticsActivity.class);
            startActivity(intent);
        });
    }
    
    /**
     * Select tab and update UI
     */
    private void selectTab(boolean isOverview) {
        // Update tab text colors
        tabOverview.setTextColor(isOverview ? Color.WHITE : Color.parseColor("#FF4B55"));
        tabCategories.setTextColor(isOverview ? Color.parseColor("#FF4B55") : Color.WHITE);
        
        // Keep bold for both tabs
        tabOverview.setTypeface(null, Typeface.BOLD);
        tabCategories.setTypeface(null, Typeface.BOLD);
        
        // Update indicator position
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tabIndicator.getLayoutParams();
        if (isOverview) {
            params.startToStart = R.id.tabOverview;
            params.endToEnd = R.id.tabOverview;
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            params.endToStart = ConstraintLayout.LayoutParams.UNSET;
        } else {
            params.startToStart = R.id.tabCategories;
            params.endToEnd = R.id.tabCategories;
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            params.endToStart = ConstraintLayout.LayoutParams.UNSET;
        }
        tabIndicator.setLayoutParams(params);
    }
    
    /**
     * Toggle calendar visibility
     */
    private void toggleCalendarVisibility() {
        isCalendarVisible = !isCalendarVisible;
        
        // Show/hide calendar but always keep stats visible
        if (isCalendarVisible) {
            calendarContainer.setVisibility(View.VISIBLE);
        } else {
            calendarContainer.setVisibility(View.GONE);
        }
        
        // Always keep stats visible
        statsContainer.setVisibility(View.VISIBLE);
    }
    
    /**
     * Get yesterday's date
     */
    private Calendar getYesterday() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        return yesterday;
    }

    /**
     * Shows date filter options in a popup menu
     */
    private void showDateFilterOptions() {
        PopupMenu popup = new PopupMenu(this, cardDateFilter);
        popup.getMenuInflater().inflate(R.menu.date_filter_menu, popup.getMenu());
        
        // Apply custom styling if needed
        try {
            // Use reflection to get the popup menu's field and apply custom styling
            java.lang.reflect.Field field = popup.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popup);
            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
            java.lang.reflect.Method method = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            method.invoke(menuPopupHelper, true);
            
            // Tint all menu icons to dark color
            for (int i = 0; i < popup.getMenu().size(); i++) {
                MenuItem item = popup.getMenu().getItem(i);
                if (item.getIcon() != null) {
                    item.getIcon().setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Set listener for option selection
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.filter_today) {
                currentDateFilter = "Today";
                currentDate = Calendar.getInstance();
            } else if (itemId == R.id.filter_yesterday) {
                currentDateFilter = "Yesterday";
                currentDate = Calendar.getInstance();
                currentDate.add(Calendar.DAY_OF_MONTH, -1);
            } else if (itemId == R.id.filter_this_week) {
                currentDateFilter = "This Week";
                currentDate = Calendar.getInstance();
                // Start of current week (Monday)
                currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            } else if (itemId == R.id.filter_last_week) {
                currentDateFilter = "Last Week";
                currentDate = Calendar.getInstance();
                // Start of last week
                currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                currentDate.add(Calendar.DAY_OF_MONTH, -7);
            } else if (itemId == R.id.filter_this_month) {
                currentDateFilter = "This Month";
                currentDate = Calendar.getInstance();
                currentDate.set(Calendar.DAY_OF_MONTH, 1);
            } else if (itemId == R.id.filter_custom) {
                // In a real app, this would show a date picker
                // For now, just use today's date
                currentDateFilter = "Custom Range";
                currentDate = Calendar.getInstance();
                return true;
            } else {
                return false;
            }
            
            tvDateFilter.setText(currentDateFilter);
            updateStats();
            return true;
        });
        
        popup.show();
    }
    
    /**
     * Update statistics display based on current date
     */
    private void updateStats() {
        TimeStats stats = statsRepository.getStatsForDate(currentDate);
        
        // Update UI
        tvProductiveTime.setText(TimeStats.formatTime(stats.getProductiveTimeMillis()));
        tvUnproductiveTime.setText(TimeStats.formatTime(stats.getUnproductiveTimeMillis()));
    }
    
    /**
     * Sets up bottom navigation with manual icons
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
            Intent intent = new Intent(StatisticsActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Highlight the first icon (Home) to match the image
        highlightNavIcon(navHome);
    }
    
    /**
     * Check if two dates represent the same day
     */
    private boolean isSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
               date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
               date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onBackPressed() {
        // Navigate back to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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