package com.example.timesync;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timesync.adapter.ScheduleAdapter;
import com.example.timesync.model.ScheduleItem;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitiesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView rvSchedule;
    private ScheduleAdapter scheduleAdapter;
    private List<ScheduleItem> scheduleItems;
    
    private TextView tvDate;
    private TextView tvTasksCountHighlight;
    private View mondayView, tuesdayView, wednesdayView, thursdayView, fridayView, saturdayView, sundayView;
    private TextView mondayNumber, tuesdayNumber, wednesdayNumber, thursdayNumber, fridayNumber, saturdayNumber, sundayNumber;
    
    // Navigation views
    private FrameLayout navActivities, navStats, navAdd, navGoals, navProfile;
    // Add Task button
    private View btnAddTask;
    // Running Subjects button
    private View btnRunningSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        
        initViews();
        setupTabLayout();
        setupWeekdayItems();
        setupDate();
        setupTasksCount();
        setupScheduleData();
        setupRecyclerView();
        setupBottomNavigation();
        setupAddTaskButton();
        setupRunningSubjectsButton();
    }
    
    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        rvSchedule = findViewById(R.id.rvSchedule);
        
        tvDate = findViewById(R.id.tvDate);
        tvTasksCountHighlight = findViewById(R.id.tvTasksCountHighlight);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnRunningSubjects = findViewById(R.id.btnRunningSubjects);
        
        // Temporarily hide the profile image until we add the CircleImageView dependency
        View profileImage = findViewById(R.id.profileImage);
        profileImage.setVisibility(View.GONE);
        
        // Initialize weekday views
        View mondayItem = findViewById(R.id.mondayItem);
        View tuesdayItem = findViewById(R.id.tuesdayItem);
        View wednesdayItem = findViewById(R.id.wednesdayItem);
        View thursdayItem = findViewById(R.id.thursdayItem);
        View fridayItem = findViewById(R.id.fridayItem);
        View saturdayItem = findViewById(R.id.saturdayItem);
        View sundayItem = findViewById(R.id.sundayItem);
        
        mondayNumber = mondayItem.findViewById(R.id.tvWeekdayNumber);
        tuesdayNumber = tuesdayItem.findViewById(R.id.tvWeekdayNumber);
        wednesdayNumber = wednesdayItem.findViewById(R.id.tvWeekdayNumber);
        thursdayNumber = thursdayItem.findViewById(R.id.tvWeekdayNumber);
        fridayNumber = fridayItem.findViewById(R.id.tvWeekdayNumber);
        saturdayNumber = saturdayItem.findViewById(R.id.tvWeekdayNumber);
        sundayNumber = sundayItem.findViewById(R.id.tvWeekdayNumber);
        
        TextView mondayName = mondayItem.findViewById(R.id.tvWeekdayName);
        TextView tuesdayName = tuesdayItem.findViewById(R.id.tvWeekdayName);
        TextView wednesdayName = wednesdayItem.findViewById(R.id.tvWeekdayName);
        TextView thursdayName = thursdayItem.findViewById(R.id.tvWeekdayName);
        TextView fridayName = fridayItem.findViewById(R.id.tvWeekdayName);
        TextView saturdayName = saturdayItem.findViewById(R.id.tvWeekdayName);
        TextView sundayName = sundayItem.findViewById(R.id.tvWeekdayName);
        
        mondayName.setText("Mon");
        tuesdayName.setText("Tue");
        wednesdayName.setText("Wed");
        thursdayName.setText("Thu");
        fridayName.setText("Fri");
        saturdayName.setText("Sat");
        sundayName.setText("Sun");
        
        // Initialize navigation views
        navActivities = findViewById(R.id.navActivities);
        navStats = findViewById(R.id.navStats);
        navAdd = findViewById(R.id.navAdd);
        navGoals = findViewById(R.id.navGoals);
        navProfile = findViewById(R.id.navProfile);
    }
    
    private void setupBottomNavigation() {
        // Activities is already selected - just add a highlight when clicked
        navActivities.setOnClickListener(v -> {
            // Already on Activities page, no need to navigate
            // Just add a visual feedback
            setHighlight(navActivities);
            
            // Optionally refresh the current page
            // recreate();
        });
        
        // Stats icon should point to DashboardActivity
        navStats.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitiesActivity.this, DashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
        
        // Add icon should open StatisticsActivity
        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitiesActivity.this, StatisticsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
        
        // Goals/Tasks icon should point to RewardsActivity
        navGoals.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitiesActivity.this, RewardsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
        
        navProfile.setOnClickListener(v -> {
            // Navigate to profile or settings page
            Intent intent = new Intent(ActivitiesActivity.this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
        
        // Highlight the activities icon by default
        setHighlight(navActivities);
    }
    
    private void setHighlight(View view) {
        // Reset all icons
        findViewById(R.id.navActivities).setBackgroundResource(0);
        findViewById(R.id.navStats).setBackgroundResource(0);
        findViewById(R.id.navAdd).setBackgroundResource(0);
        findViewById(R.id.navGoals).setBackgroundResource(0);
        findViewById(R.id.navProfile).setBackgroundResource(0);
        
        // Get the ImageView in each nav container
        ImageView activitiesIcon = (ImageView) ((FrameLayout)findViewById(R.id.navActivities)).getChildAt(0);
        ImageView statsIcon = (ImageView) ((FrameLayout)findViewById(R.id.navStats)).getChildAt(0);
        ImageView addIcon = (ImageView) ((FrameLayout)findViewById(R.id.navAdd)).getChildAt(0);
        ImageView goalsIcon = (ImageView) ((FrameLayout)findViewById(R.id.navGoals)).getChildAt(0);
        ImageView profileIcon = (ImageView) ((FrameLayout)findViewById(R.id.navProfile)).getChildAt(0);
        
        // Reset all icon tints
        activitiesIcon.setColorFilter(null);
        statsIcon.setColorFilter(null);
        addIcon.setColorFilter(null);
        goalsIcon.setColorFilter(null);
        profileIcon.setColorFilter(null);
        
        // Set selected icon tint to blue
        ImageView selectedIcon = (ImageView) ((FrameLayout)view).getChildAt(0);
        selectedIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_light));
        
        // Flash effect or subtle animation to show the icon was clicked
        selectedIcon.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100)
            .withEndAction(() -> 
                selectedIcon.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
            ).start();
    }
    
    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection
                if (tab.getPosition() == 0) {
                    // Time Table tab selected
                } else {
                    // Statistics tab selected
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed for this implementation
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed for this implementation
            }
        });
    }
    
    private void setupWeekdayItems() {
        // Set sample dates for weekdays
        mondayNumber.setText("14");
        tuesdayNumber.setText("15");
        wednesdayNumber.setText("16");
        thursdayNumber.setText("17");
        fridayNumber.setText("18");
        saturdayNumber.setText("19");
        sundayNumber.setText("20");
        
        // Make all date numbers bold for better visibility
        mondayNumber.setTypeface(null, Typeface.BOLD);
        tuesdayNumber.setTypeface(null, Typeface.BOLD);
        wednesdayNumber.setTypeface(null, Typeface.BOLD);
        thursdayNumber.setTypeface(null, Typeface.BOLD);
        fridayNumber.setTypeface(null, Typeface.BOLD);
        saturdayNumber.setTypeface(null, Typeface.BOLD);
        sundayNumber.setTypeface(null, Typeface.BOLD);
        
        // Set Thursday (current day) as selected
        thursdayNumber.setSelected(true);
    }
    
    private void setupDate() {
        // Set current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM", Locale.getDefault());
        tvDate.setText(dateFormat.format(new Date()));
    }
    
    private void setupTasksCount() {
        // Set tasks count
        tvTasksCountHighlight.setText("4 tasks today");
    }
    
    private void setupScheduleData() {
        scheduleItems = new ArrayList<>();
        
        // Add Physics class
        scheduleItems.add(new ScheduleItem(
                "9:30", "10:20", 
                "Physics", "Chapter 3: Force",
                "Alex Jesus", "Google Meet",
                getResources().getColor(R.color.physics_bg),
                R.drawable.profile_placeholder, 
                R.drawable.ic_google_meet
        ));
        
        // Add Geography class
        scheduleItems.add(new ScheduleItem(
                "11:00", "11:50", 
                "Geography", "Chapter 12: Soil Profile",
                "Jenifer Clark", "Zoom",
                getResources().getColor(R.color.geography_bg),
                R.drawable.profile_placeholder, 
                R.drawable.ic_zoom
        ));
        
        // Add Assignment
        scheduleItems.add(new ScheduleItem(
                "12:20", "13:00", 
                "Assignment", "World Regional Pattern",
                "Alexa Tenorio", "Google Docs",
                getResources().getColor(R.color.assignment_bg),
                R.drawable.profile_placeholder, 
                R.drawable.ic_google_docs,
                true
        ));
    }
    
    private void setupRecyclerView() {
        scheduleAdapter = new ScheduleAdapter(this, scheduleItems);
        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        rvSchedule.setAdapter(scheduleAdapter);
    }

    private void setupAddTaskButton() {
        btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitiesActivity.this, AddTaskActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void setupRunningSubjectsButton() {
        btnRunningSubjects.setOnClickListener(v -> {
            // Navigate to RunningSubjectsActivity instead of showing dialog
            try {
                Intent intent = new Intent(ActivitiesActivity.this, RunningSubjectsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                // Add a Toast message to confirm button click
                Toast.makeText(this, "Opening Running Subjects...", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Log any errors
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
} 