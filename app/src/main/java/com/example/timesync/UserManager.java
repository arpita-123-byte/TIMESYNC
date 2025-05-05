package com.example.timesync;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manager class to handle user authentication and registration.
 */
public class UserManager {
    
    private static final String PREF_NAME = "TimeSync_Users";
    private static final String KEY_PREFIX_EMAIL = "user_email_";
    private static final String KEY_PREFIX_PASSWORD = "user_password_";
    private static final String KEY_PREFIX_NAME = "user_name_";
    private static final String KEY_PREFIX_PHONE = "user_phone_";
    
    private static UserManager instance;
    private final SharedPreferences preferences;
    
    private UserManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Get the singleton instance of UserManager
     */
    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Check if a user with the given email exists
     */
    public boolean isUserRegistered(String email) {
        return preferences.contains(KEY_PREFIX_EMAIL + email.toLowerCase());
    }
    
    /**
     * Register a new user
     */
    public boolean registerUser(String name, String phone, String email, String password) {
        email = email.toLowerCase();
        
        // Check if user already exists
        if (isUserRegistered(email)) {
            return false;
        }
        
        // Save user data
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PREFIX_EMAIL + email, email);
        editor.putString(KEY_PREFIX_PASSWORD + email, password);
        editor.putString(KEY_PREFIX_NAME + email, name);
        editor.putString(KEY_PREFIX_PHONE + email, phone);
        return editor.commit();
    }
    
    /**
     * Authenticate a user with email and password
     */
    public boolean authenticateUser(String email, String password) {
        email = email.toLowerCase();
        
        // Check if user exists
        if (!isUserRegistered(email)) {
            return false;
        }
        
        // Check if password matches
        String savedPassword = preferences.getString(KEY_PREFIX_PASSWORD + email, "");
        return password.equals(savedPassword);
    }
    
    /**
     * Update user's password
     */
    public boolean updatePassword(String email, String currentPassword, String newPassword) {
        email = email.toLowerCase();
        
        // Verify user exists and current password is correct
        if (!authenticateUser(email, currentPassword)) {
            return false;
        }
        
        // Get existing user data
        String name = getUserName(email);
        String phone = getUserPhone(email);
        
        // Update password
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PREFIX_PASSWORD + email, newPassword);
        return editor.commit();
    }
    
    /**
     * Get user's name by email
     */
    public String getUserName(String email) {
        return preferences.getString(KEY_PREFIX_NAME + email.toLowerCase(), "");
    }
    
    /**
     * Get user's phone by email
     */
    public String getUserPhone(String email) {
        return preferences.getString(KEY_PREFIX_PHONE + email.toLowerCase(), "");
    }
    
    /**
     * Update user profile information
     */
    public boolean updateUserProfile(String email, String name, String phone) {
        email = email.toLowerCase();
        
        // Check if user exists
        if (!isUserRegistered(email)) {
            return false;
        }
        
        // Get current password
        String password = preferences.getString(KEY_PREFIX_PASSWORD + email, "");
        
        // Update user data
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PREFIX_NAME + email, name);
        editor.putString(KEY_PREFIX_PHONE + email, phone);
        return editor.commit();
    }
} 