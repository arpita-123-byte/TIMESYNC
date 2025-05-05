package com.example.timesync;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.TextView;

/**
 * ProfileActivity displays the user profile information and settings.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupTopBar();
        setupProfileSection();
        setupMenuItems();
        setupBottomNavigation();
    }

    /**
     * Sets up the top bar with back button and settings.
     */
    private void setupTopBar() {
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnSettings = findViewById(R.id.btnSettings);

        btnBack.setOnClickListener(v -> onBackPressed());
        
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Sets up the profile section with edit button.
     */
    private void setupProfileSection() {
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            // Launch EditProfileActivity
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            // Pass current profile data if available
            TextView profileName = findViewById(R.id.profileName);
            TextView profileEmail = findViewById(R.id.profileEmail);
            
            if (profileName != null) {
                intent.putExtra("name", profileName.getText().toString());
            }
            
            if (profileEmail != null) {
                intent.putExtra("email", profileEmail.getText().toString());
            }
            
            startActivityForResult(intent, 1001);
        });
        
        findViewById(R.id.btnCamera).setOnClickListener(v -> {
            Toast.makeText(this, "Change photo clicked", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Sets up menu items with click listeners.
     */
    private void setupMenuItems() {
        // Favourites
        findViewById(R.id.favouritesItem).setOnClickListener(v -> {
            Toast.makeText(this, "Favourites clicked", Toast.LENGTH_SHORT).show();
        });
        
        // Downloads
        findViewById(R.id.downloadsItem).setOnClickListener(v -> {
            Toast.makeText(this, "Downloads clicked", Toast.LENGTH_SHORT).show();
        });
        
        // FAQs
        findViewById(R.id.faqsItem).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FAQActivity.class);
            startActivity(intent);
        });
        
        // Contact Us
        findViewById(R.id.contactUsItem).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });
        
        // Subscription
        findViewById(R.id.subscriptionItem).setOnClickListener(v -> {
            Toast.makeText(this, "Subscription clicked", Toast.LENGTH_SHORT).show();
        });
        
        // Saved Schedules
        findViewById(R.id.savedSchedulesItem).setOnClickListener(v -> {
            Toast.makeText(this, "Saved Schedules clicked", Toast.LENGTH_SHORT).show();
        });
        
        // Clear Cache
        findViewById(R.id.clearCacheItem).setOnClickListener(v -> {
            Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show();
        });
        
        // Clear History
        findViewById(R.id.clearHistoryItem).setOnClickListener(v -> {
            Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
        });
        
        // Log Out
        findViewById(R.id.logOutItem).setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            
            // Navigate to Sign In screen
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            // Clear all activities in the stack to prevent going back to profile after logout
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Sets up bottom navigation with manual icons for guaranteed visibility.
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
            Intent intent = new Intent(ProfileActivity.this, ActivitiesActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Stats icon - navigate to DashboardActivity
        navStats.setOnClickListener(v -> {
            highlightNavIcon(navStats);
            Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Add icon - navigate to StatisticsActivity
        navAdd.setOnClickListener(v -> {
            highlightNavIcon(navAdd);
            Intent intent = new Intent(ProfileActivity.this, StatisticsActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Tasks icon - navigate to Rewards
        navTasks.setOnClickListener(v -> {
            highlightNavIcon(navTasks);
            Intent intent = new Intent(ProfileActivity.this, RewardsActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Profile icon - already on Profile page
        navProfile.setOnClickListener(v -> {
            highlightNavIcon(navProfile);
            // Already on Profile page
        });
        
        // Highlight the profile icon by default
        highlightNavIcon(navProfile);
    }
    
    /**
     * Highlights the selected navigation icon and clears others.
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            // Handle profile update result
            String firstName = data.getStringExtra("firstName");
            String lastName = data.getStringExtra("lastName");
            String email = data.getStringExtra("email");
            String profileImageUri = data.getStringExtra("profileImage");
            
            // Update UI with new profile data
            TextView profileName = findViewById(R.id.profileName);
            TextView profileEmail = findViewById(R.id.profileEmail);
            ImageView profileImage = findViewById(R.id.profileImage);
            
            if (profileName != null && firstName != null) {
                String fullName = firstName;
                if (lastName != null && !lastName.isEmpty()) {
                    fullName += " " + lastName;
                }
                profileName.setText(fullName);
            }
            
            if (profileEmail != null && email != null) {
                profileEmail.setText(email);
            }
            
            if (profileImage != null && profileImageUri != null) {
                try {
                    Uri imageUri = Uri.parse(profileImageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    profileImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
} 