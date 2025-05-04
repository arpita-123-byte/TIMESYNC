package com.example.timesync;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.timesync.CircularScoreView;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.Gravity;
import android.animation.ObjectAnimator;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.animation.DecelerateInterpolator;
import android.graphics.Typeface;
import android.os.Handler;
import android.content.res.ColorStateList;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * MainActivity displays the statistics dashboard with tabs, overview, and app usage list.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView appUsageRecycler;
    private TextView tabOverview, tabCategories, title;
    private TextView dateButton;
    private ImageButton btnCalendar;
    private CardView cardDateFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTopBar();
        setupTabs();
        setupScoreChart();
        setupBottomNavigation();
        
        // Delay loading app list to ensure scrolling works properly
        new Handler().postDelayed(() -> {
            setupAppList();
            
            // Enable scrolling to see all apps
            androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.appListScrollView);
            scrollView.setFocusable(true);
            scrollView.setFocusableInTouchMode(true);
            scrollView.setNestedScrollingEnabled(true);
            scrollView.setVerticalScrollBarEnabled(true);
            
            // Make sure scroll indicators are visible
            scrollView.setScrollbarFadingEnabled(false);
            scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            
            // Make the scroll view taller to allow full scrolling
            ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            scrollView.setLayoutParams(layoutParams);
            
            // Increase bottom padding to ensure all items are visible with bottom nav
            LinearLayout contentLayout = findViewById(R.id.appListContainer);
            contentLayout.setPadding(contentLayout.getPaddingLeft(), 
                                    contentLayout.getPaddingTop(),
                                    contentLayout.getPaddingRight(), 
                                    contentLayout.getPaddingBottom() + 150);
            
            // Add a scroll demonstration that shows all apps
            new Handler().postDelayed(() -> {
                // First scroll to show middle entries
                scrollView.smoothScrollBy(0, 200);
                
                // Then scroll further to show the last entries
                new Handler().postDelayed(() -> {
                    scrollView.smoothScrollBy(0, 300);
                    
                    // Finally scroll back to top after showing everything
                    new Handler().postDelayed(() -> scrollView.smoothScrollTo(0, 0), 1000);
                }, 1000);
            }, 500);
        }, 100);
    }

    /**
     * Sets up the top bar with title and date button.
     */
    private void setupTopBar() {
        title = findViewById(R.id.statisticsTitle);
        title.setText("Statistics");

        dateButton = findViewById(R.id.dateButton);
        btnCalendar = findViewById(R.id.btnCalendar);
        cardDateFilter = findViewById(R.id.cardDateFilter);
        
        // Set up calendar button click
        btnCalendar.setOnClickListener(v -> {
            // Navigate to StatisticsActivity
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
            // No finish() to allow back navigation
        });
        
        // Set up date filter dropdown
        cardDateFilter.setOnClickListener(v -> {
            showDateFilterOptions();
        });
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
                dateButton.setText("Today");
                return true;
            } else if (itemId == R.id.filter_yesterday) {
                dateButton.setText("Yesterday");
                return true;
            } else if (itemId == R.id.filter_this_week) {
                dateButton.setText("This Week");
                return true;
            } else if (itemId == R.id.filter_last_week) {
                dateButton.setText("Last Week");
                return true;
            } else if (itemId == R.id.filter_this_month) {
                dateButton.setText("This Month");
                return true;
            } else if (itemId == R.id.filter_custom) {
                // In a real app, this would show a date picker
                dateButton.setText("Custom Range");
                return true;
            }
            
            return false;
        });
        
        popup.show();
    }

    /**
     * Sets up the custom tabs and their click listeners.
     */
    private void setupTabs() {
        tabOverview = findViewById(R.id.tabOverview);
        tabCategories = findViewById(R.id.tabCategories);

        tabOverview.setOnClickListener(v -> selectTab(true));
        tabCategories.setOnClickListener(v -> selectTab(false));

        // Default: Overview selected
        selectTab(true);
    }

    /**
     * Handles tab selection UI.
     * @param isOverview true for Overview, false for Categories
     */
    private void selectTab(boolean isOverview) {
        if (isOverview) {
            tabOverview.setTextColor(Color.WHITE);
            tabCategories.setTextColor(Color.parseColor("#FF4B55"));
            // TODO: Show overview content, hide categories
        } else {
            tabOverview.setTextColor(Color.parseColor("#FF4B55"));
            tabCategories.setTextColor(Color.WHITE);
            // Navigate to App Statistics Activity (third page)
            Intent intent = new Intent(this, AppStatisticsActivity.class);
            startActivity(intent);
        }
        
        // Animate or move the tab indicator
        View tabIndicator = findViewById(R.id.tabIndicator);
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
     * Sets up the app usage list.
     */
    private void setupAppList() {
        // Get the container
        LinearLayout appListContainer = findViewById(R.id.appListContainer);
        appListContainer.removeAllViews();
        
        // Get app usage data - using lots of items to ensure scrolling
        List<AppUsage> appUsageList = getDummyAppUsageData();
        
        // Add a header to the app list - smaller to save space
        TextView header = new TextView(this);
        header.setText("App Usage");
        header.setTextSize(18); // Smaller text size
        header.setTextColor(Color.parseColor("#111B2E"));
        header.setPadding(16, 8, 16, 8);
        header.setTypeface(null, Typeface.BOLD);
        appListContainer.addView(header);
        
        // Create a LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(this);
        
        // Add each app item to the container
        for (AppUsage app : appUsageList) {
            View itemView;
            
            // Use special layouts for YouTube and Twitter
            if (app.appName.equals("YouTube")) {
                itemView = inflater.inflate(R.layout.item_youtube_usage, appListContainer, false);
            } else if (app.appName.equals("Twitter")) {
                itemView = inflater.inflate(R.layout.item_twitter_usage, appListContainer, false);
            } else {
                itemView = inflater.inflate(R.layout.item_app_usage, appListContainer, false);
            }
            
            // Find views within the item
            ImageView icon = itemView.findViewById(R.id.appIcon);
            TextView name = itemView.findViewById(R.id.appName);
            TextView time = itemView.findViewById(R.id.appTime);
            CircularProgressView progressCircle = itemView.findViewById(R.id.appProgressCircle);
            
            // Set data to views
            icon.setImageResource(app.iconRes);
            name.setText(app.appName);
            time.setText(app.usageTime);
            progressCircle.setProgress(app.productivity);
            
            // Special handling for YouTube to ensure its progress is visible
            if (app.appName.equals("YouTube")) {
                // Use a more noticeable color for YouTube's low productivity
                progressCircle.setProgressColor(Color.parseColor("#FF3D00"));
                
                // Add a subtle highlight effect
                progressCircle.setElevation(4f);
            }
            
            // Special handling for Twitter to make it more visible
            if (app.appName.equals("Twitter")) {
                // Use a distinct Twitter blue for the progress circle
                progressCircle.setProgressColor(Color.parseColor("#1DA1F2"));
                
                // Add a highlight effect
                progressCircle.setElevation(4f);
            }
            
            // Add spacing between items to make them visually separated
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 4, 0, 4); // Small margins but enough to see separation
            itemView.setLayoutParams(params);
            
            // Add the view to container
            appListContainer.addView(itemView);
        }
        
        // No need for an extra footer that creates empty space
    }

    /**
     * Sets up the circular score chart.
     */
    private void setupScoreChart() {
        CircularScoreView scoreView = findViewById(R.id.scoreChart);
        scoreView.setScore(56.9f);
        scoreView.setProductiveColor(Color.parseColor("#2196F3"));
        scoreView.setUnproductiveColor(Color.parseColor("#F44336"));
    }

    /**
     * Dummy data for app usage list - showing YouTube and Twitter prominently
     */
    private List<AppUsage> getDummyAppUsageData() {
        List<AppUsage> list = new ArrayList<>();
        
        // Putting YouTube and Twitter at the top for better visibility
        list.add(new AppUsage("YouTube", "03h 15m", R.drawable.ic_youtube_new, 15));
        list.add(new AppUsage("Twitter", "01h 10m", R.drawable.ic_twitter_new, 35));
        list.add(new AppUsage("Spotify", "02h 30m", R.drawable.ic_spotify, 20));
        list.add(new AppUsage("Facebook", "01h 25m", R.drawable.ic_facebook_white, 40));
        list.add(new AppUsage("Whatsapp", "02h 30m", R.drawable.ic_whatsapp_new, 30));
        list.add(new AppUsage("Instagram", "01h 45m", R.drawable.ic_instagram_new, 25));
        
        return list;
    }

    /**
     * Sets up bottom navigation with manual icons for guaranteed visibility
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
            Intent intent = new Intent(MainActivity.this, ActivitiesActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Stats icon - navigate to DashboardActivity
        navStats.setOnClickListener(v -> {
            highlightNavIcon(navStats);
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            // Don't finish MainActivity to allow back navigation
        });
        
        // Add icon - opens MainActivity (current page)
        navAdd.setOnClickListener(v -> {
            // Already on MainActivity
            highlightNavIcon(navAdd);
        });
        
        // Tasks icon - navigate to Rewards
        navTasks.setOnClickListener(v -> {
            highlightNavIcon(navTasks);
            Intent intent = new Intent(MainActivity.this, RewardsActivity.class);
            startActivity(intent);
            // Don't finish MainActivity to allow back navigation
        });
        
        // Profile icon - navigate to a profile page (would be implemented later)
        navProfile.setOnClickListener(v -> {
            highlightNavIcon(navProfile);
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
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
        // Reset all icon backgrounds
        findViewById(R.id.navHomeContainer).setBackgroundResource(0);
        findViewById(R.id.navStatsContainer).setBackgroundResource(0);
        findViewById(R.id.navAddContainer).setBackgroundResource(0);
        findViewById(R.id.navTasksContainer).setBackgroundResource(0);
        findViewById(R.id.navProfileContainer).setBackgroundResource(0);
        
        // Reset all icon tints
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