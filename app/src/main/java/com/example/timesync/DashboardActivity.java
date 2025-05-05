package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.animation.ObjectAnimator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TextView clockTimeText;
    private TextView todayHoursText;
    private TextView weekHoursText;
    private TextView monthHoursText;
    private TextView currentDateText;
    private TextView statusText;
    private CardView clockInButton;
    
    // Add navigation icons as class fields
    private ImageView navHome;
    private ImageView navStats;
    private ImageView navAdd;
    private ImageView navTasks;
    private ImageView navProfile;
    
    private boolean isClockedIn = false;
    private long clockInTime = 0;
    private Handler timeUpdateHandler = new Handler();
    private Runnable timeUpdateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        initializeViews();
        
        // Start the clock
        startClock();
        
        // Set up click listeners
        setupClickListeners();
        
        // Set up data
        setupData();
        
        // Setup bottom navigation
        setupBottomNavigation();
    }
    
    private void initializeViews() {
        clockTimeText = findViewById(R.id.clockTimeText);
        todayHoursText = findViewById(R.id.todayHoursText);
        weekHoursText = findViewById(R.id.weekHoursText);
        monthHoursText = findViewById(R.id.monthHoursText);
        currentDateText = findViewById(R.id.currentDateText);
        statusText = findViewById(R.id.statusText);
        clockInButton = findViewById(R.id.clockInButton);
    }
    
    private void startClock() {
        // Update clock every second
        timeUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateCurrentTime();
                timeUpdateHandler.postDelayed(this, 1000);
            }
        };
        timeUpdateHandler.post(timeUpdateRunnable);
    }
    
    private void updateCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        clockTimeText.setText(currentTime);
        
        // Update current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        currentDateText.setText(currentDate);
        
        // Update today's hours if clocked in
        if (isClockedIn) {
            long currentTimeMillis = System.currentTimeMillis();
            long elapsedTimeMillis = currentTimeMillis - clockInTime;
            updateWorkHours(elapsedTimeMillis);
        }
    }
    
    private void updateWorkHours(long elapsedTimeMillis) {
        // Convert to hours and minutes
        int hours = (int) (elapsedTimeMillis / (1000 * 60 * 60));
        int minutes = (int) ((elapsedTimeMillis / (1000 * 60)) % 60);
        
        // Format and display
        String hoursFormatted = String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
        todayHoursText.setText(hoursFormatted);
    }
    
    private void setupClickListeners() {
        // Clock in/out button
        clockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleClockInOut();
            }
        });
    }
    
    private void toggleClockInOut() {
        TextView buttonText = (TextView) ((CardView) clockInButton).getChildAt(0);
        
        if (!isClockedIn) {
            // Clock in
            isClockedIn = true;
            clockInTime = System.currentTimeMillis();
            buttonText.setText("CLOCK OUT");
            statusText.setText("Working");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            Toast.makeText(this, "Clocked in successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Clock out
            isClockedIn = false;
            buttonText.setText("CLOCK IN");
            statusText.setText("Checked Out");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            Toast.makeText(this, "Clocked out successfully", Toast.LENGTH_SHORT).show();
            
            // Reset today's hours
            todayHoursText.setText("00:00");
        }
    }
    
    private void setupData() {
        // Set status
        statusText.setText("Checked Out");
        
        // Set dummy data for week hours
        weekHoursText.setText("16:30");
        
        // Set dummy data for month hours
        monthHoursText.setText("76:00");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callback to prevent memory leaks
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);
    }
    
    /**
     * Sets up bottom navigation with manual icons for guaranteed visibility
     */
    private void setupBottomNavigation() {
        // Initialize navigation icons
        navHome = findViewById(R.id.navHome);
        navStats = findViewById(R.id.navStats);
        navAdd = findViewById(R.id.navAdd);
        navTasks = findViewById(R.id.navTasks);
        navProfile = findViewById(R.id.navProfile);
        
        // Home icon - navigate to ActivitiesActivity
        navHome.setOnClickListener(v -> {
            highlightNavIcon(navHome);
            Intent intent = new Intent(DashboardActivity.this, ActivitiesActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Stats icon - stay on DashboardActivity (current page)
        navStats.setOnClickListener(v -> {
            // Already on Dashboard
            highlightNavIcon(navStats);
        });
        
        // Add icon - navigate to StatisticsActivity
        navAdd.setOnClickListener(v -> {
            highlightNavIcon(navAdd);
            Intent intent = new Intent(DashboardActivity.this, StatisticsActivity.class);
            startActivity(intent);
            finish(); // Finish this activity for consistent navigation
        });
        
        // Tasks icon - navigate to Rewards
        navTasks.setOnClickListener(v -> {
            highlightNavIcon(navTasks);
            Intent intent = new Intent(DashboardActivity.this, RewardsActivity.class);
            startActivity(intent);
            // Don't finish this activity to allow back navigation
        });
        
        // Profile icon - navigate to a profile page (would be implemented later)
        navProfile.setOnClickListener(v -> {
            highlightNavIcon(navProfile);
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
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
        navHome.setColorFilter(null);
        navStats.setColorFilter(null);
        navAdd.setColorFilter(null);
        navTasks.setColorFilter(null);
        navProfile.setColorFilter(null);
        
        // Set selected icon tint to blue
        selectedIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_light));
        
        // Add animation effect
        ObjectAnimator.ofFloat(selectedIcon, "scaleX", 1f, 1.2f, 1f).setDuration(300).start();
        ObjectAnimator.ofFloat(selectedIcon, "scaleY", 1f, 1.2f, 1f).setDuration(300).start();
    }
} 