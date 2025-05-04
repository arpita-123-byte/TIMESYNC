package com.example.timesync;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GoalAdapter goalAdapter;
    private List<Goal> goalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        recyclerView = findViewById(R.id.goals_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize goals data
        goalList = new ArrayList<>();
        initializeGoals();

        // Setup adapter
        goalAdapter = new GoalAdapter(goalList);
        recyclerView.setAdapter(goalAdapter);

        // Back button click handler
        findViewById(R.id.back_button).setOnClickListener(v -> {
            navigateBackToAppStats();
        });

        // Add goal button click handler
        findViewById(R.id.add_goal_button).setOnClickListener(v -> {
            Toast.makeText(this, "Add Goal feature coming soon", Toast.LENGTH_SHORT).show();
        });
        
        // Set up bottom navigation
        setupBottomNavigation();
    }
    
    private void navigateBackToAppStats() {
        Intent intent = new Intent(this, AppStatisticsActivity.class);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        navigateBackToAppStats();
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
            finish();
        });
        
        // Add icon - navigate to MainActivity
        navAdd.setOnClickListener(v -> {
            highlightNavIcon(navAdd);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Tasks icon - already on GoalsActivity, just highlight
        navTasks.setOnClickListener(v -> {
            highlightNavIcon(navTasks);
        });
        
        // Profile icon - navigate to profile
        navProfile.setOnClickListener(v -> {
            highlightNavIcon(navProfile);
            Intent intent = new Intent(GoalsActivity.this, ProfileActivity.class);
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

    private void initializeGoals() {
        // Add sample goals as shown in the image
        goalList.add(new Goal("Study", "Spend more than 5h", "09h 41m", true, R.drawable.ic_study));
        goalList.add(new Goal("Spotify", "Spend less than 2h", "02h 41m", false, R.drawable.ic_spotify));
        goalList.add(new Goal("LinkedIn", "Spend less than 2h", "01h 41m", true, R.drawable.ic_linkedin));
        goalList.add(new Goal("Gym", "Spend more than 1h", "03h 41m", true, R.drawable.ic_gym));
        goalList.add(new Goal("Youtube", "Spend less than 2h", "03h 41m", false, R.drawable.ic_youtube));
        goalList.add(new Goal("Activity", "Spend less than 1h", "03h 41m", false, R.drawable.ic_activity));
    }

    // Goal model class
    public static class Goal {
        private String name;
        private String target;
        private String currentTime;
        private boolean isProductive;
        private int iconResId;

        public Goal(String name, String target, String currentTime, boolean isProductive, int iconResId) {
            this.name = name;
            this.target = target;
            this.currentTime = currentTime;
            this.isProductive = isProductive;
            this.iconResId = iconResId;
        }

        public String getName() {
            return name;
        }

        public String getTarget() {
            return target;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public boolean isProductive() {
            return isProductive;
        }

        public int getIconResId() {
            return iconResId;
        }
    }

    // Goal adapter for the RecyclerView
    public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

        private List<Goal> goals;

        public GoalAdapter(List<Goal> goals) {
            this.goals = goals;
        }

        @NonNull
        @Override
        public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
            return new GoalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
            Goal goal = goals.get(position);
            
            holder.nameTextView.setText(goal.getName());
            holder.targetTextView.setText(goal.getTarget());
            holder.timeTextView.setText(goal.getCurrentTime());
            holder.iconImageView.setImageResource(goal.getIconResId());
            
            // Set productive or unproductive status
            if (goal.isProductive()) {
                holder.statusTextView.setText("Productive");
                holder.statusTextView.setTextColor(getResources().getColor(R.color.productive_color));
                holder.statusIconView.setImageResource(R.drawable.ic_productive);
            } else {
                holder.statusTextView.setText("Unproductive");
                holder.statusTextView.setTextColor(getResources().getColor(R.color.unproductive_color));
                holder.statusIconView.setImageResource(R.drawable.ic_unproductive);
            }

            // Set options menu click listener
            holder.optionsButton.setOnClickListener(v -> {
                Toast.makeText(GoalsActivity.this, "Options for " + goal.getName(), Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return goals.size();
        }

        public class GoalViewHolder extends RecyclerView.ViewHolder {
            ImageView iconImageView;
            TextView nameTextView;
            TextView targetTextView;
            TextView timeTextView;
            TextView statusTextView;
            ImageView statusIconView;
            View optionsButton;

            public GoalViewHolder(@NonNull View itemView) {
                super(itemView);
                iconImageView = itemView.findViewById(R.id.goal_icon);
                nameTextView = itemView.findViewById(R.id.goal_name);
                targetTextView = itemView.findViewById(R.id.goal_target);
                timeTextView = itemView.findViewById(R.id.goal_time);
                statusTextView = itemView.findViewById(R.id.goal_status);
                statusIconView = itemView.findViewById(R.id.goal_status_icon);
                optionsButton = itemView.findViewById(R.id.goal_options);
            }
        }
    }
} 