package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;

public class RewardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        // Set up the profile name
        TextView profileName = findViewById(R.id.profileName);
        profileName.setText("Sabrina Aryan");
        
        // Set up level text
        TextView levelText = findViewById(R.id.levelText);
        levelText.setText("Lvl 1");
        
        // Set up reward message
        TextView rewardTitle = findViewById(R.id.rewardTitle);
        rewardTitle.setText("Reward yourself");
        
        TextView rewardSubtitle = findViewById(R.id.rewardSubtitle);
        rewardSubtitle.setText("Have a treat");
        
        // Set up progress data for Study
        TextView studyProgressText = findViewById(R.id.studyProgressText);
        studyProgressText.setText("29/50");
        
        // Set up progress data for Experience
        TextView experienceProgressText = findViewById(R.id.experienceProgressText);
        experienceProgressText.setText("12/25");
        
        // Set up coin balance
        TextView coinBalanceText = findViewById(R.id.coinBalanceText);
        coinBalanceText.setText("2.02");
        
        // Set up gem balance
        TextView gemBalanceText = findViewById(R.id.gemBalanceText);
        gemBalanceText.setText("0");
        
        // Set up rewards card balance
        TextView rewardCardBalance = findViewById(R.id.rewardCardBalance);
        rewardCardBalance.setText("0");
        
        // Set up reward buttons click listeners
        setupRewardItems();
        
        // Set up bottom navigation
        setupBottomNavigation();
        
        // Highlight the rewards icon (4th icon)
        highlightNavIcon(findViewById(R.id.navTasks));
    }
    
    private void setupRewardItems() {
        // Set up click listeners for reward buttons
        LinearLayout reward10 = findViewById(R.id.reward10);
        LinearLayout reward20 = findViewById(R.id.reward20);
        LinearLayout reward30 = findViewById(R.id.reward30);
        LinearLayout reward40 = findViewById(R.id.reward40);
        
        reward10.setOnClickListener(v -> {
            // Handle 10 coins reward redemption
            // This would be implemented with actual logic in a real app
        });
        
        reward20.setOnClickListener(v -> {
            // Handle 20 coins reward redemption
        });
        
        reward30.setOnClickListener(v -> {
            // Handle 30 coins reward redemption
        });
        
        reward40.setOnClickListener(v -> {
            // Handle 40 coins reward redemption
        });
    }
    
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
            // Don't finish this activity to allow back navigation
        });
        
        // Add icon - navigate to StatisticsActivity
        navAdd.setOnClickListener(v -> {
            highlightNavIcon(navAdd);
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
            finish(); // Finish this activity for consistent navigation
        });
        
        // Tasks icon - already on this page (RewardsActivity)
        navTasks.setOnClickListener(v -> {
            // Already on RewardsActivity
            highlightNavIcon(navTasks);
        });
        
        // Profile icon - navigate to profile page
        navProfile.setOnClickListener(v -> {
            highlightNavIcon(navProfile);
            Intent intent = new Intent(RewardsActivity.this, ProfileActivity.class);
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