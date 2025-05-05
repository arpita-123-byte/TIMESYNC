package com.example.timesync;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetNewPasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button savePasswordButton;
    private ImageButton backButton;
    private ImageButton currentPasswordVisibilityButton;
    private ImageButton newPasswordVisibilityButton;
    private ImageButton confirmPasswordVisibilityButton;
    
    private boolean isCurrentPasswordVisible = false;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        // Get user email from intent extras
        if (getIntent().hasExtra("email")) {
            userEmail = getIntent().getStringExtra("email");
        } else {
            Toast.makeText(this, "Error: User email not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        initViews();
        setupListeners();
    }

    private void initViews() {
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        savePasswordButton = findViewById(R.id.savePasswordButton);
        backButton = findViewById(R.id.backButton);
        currentPasswordVisibilityButton = findViewById(R.id.currentPasswordVisibilityButton);
        newPasswordVisibilityButton = findViewById(R.id.newPasswordVisibilityButton);
        confirmPasswordVisibilityButton = findViewById(R.id.confirmPasswordVisibilityButton);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());
        
        currentPasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(
                currentPasswordEditText, 
                isCurrentPasswordVisible, 
                currentPasswordVisibilityButton));
        
        newPasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(
                newPasswordEditText, 
                isNewPasswordVisible, 
                newPasswordVisibilityButton));
        
        confirmPasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(
                confirmPasswordEditText, 
                isConfirmPasswordVisible, 
                confirmPasswordVisibilityButton));
        
        savePasswordButton.setOnClickListener(v -> attemptChangePassword());
    }

    private void togglePasswordVisibility(EditText editText, boolean isVisible, ImageButton button) {
        if (isVisible) {
            // Hide password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            // Update button icon
            button.setImageResource(R.drawable.ic_visibility_off);
            if (editText == currentPasswordEditText) {
                isCurrentPasswordVisible = false;
            } else if (editText == newPasswordEditText) {
                isNewPasswordVisible = false;
            } else if (editText == confirmPasswordEditText) {
                isConfirmPasswordVisible = false;
            }
        } else {
            // Show password
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // Update button icon
            button.setImageResource(R.drawable.ic_visibility);
            if (editText == currentPasswordEditText) {
                isCurrentPasswordVisible = true;
            } else if (editText == newPasswordEditText) {
                isNewPasswordVisible = true;
            } else if (editText == confirmPasswordEditText) {
                isConfirmPasswordVisible = true;
            }
        }
        // Keep cursor at the end of the text
        editText.setSelection(editText.getText().length());
    }

    private void attemptChangePassword() {
        // Reset errors
        currentPasswordEditText.setError(null);
        newPasswordEditText.setError(null);
        confirmPasswordEditText.setError(null);

        // Get values
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate current password
        if (TextUtils.isEmpty(currentPassword)) {
            currentPasswordEditText.setError("Current password is required");
            currentPasswordEditText.requestFocus();
            return;
        }

        // Validate new password
        if (TextUtils.isEmpty(newPassword)) {
            newPasswordEditText.setError("New password is required");
            newPasswordEditText.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            newPasswordEditText.setError("Password must be at least 6 characters");
            newPasswordEditText.requestFocus();
            return;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Please confirm your new password");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // Verify current password and update to new password
        UserManager userManager = UserManager.getInstance(this);
        
        // Update password
        boolean success = userManager.updatePassword(userEmail, currentPassword, newPassword);
        
        if (success) {
            Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            currentPasswordEditText.setError("Incorrect current password");
            currentPasswordEditText.requestFocus();
        }
    }
} 