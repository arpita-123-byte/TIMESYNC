package com.example.timesync;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private ImageButton backButton;
    private ImageButton passwordVisibilityButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI elements
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        signInTextView = findViewById(R.id.signInTextView);
        backButton = findViewById(R.id.backButton);
        passwordVisibilityButton = findViewById(R.id.passwordVisibilityButton);

        // Set click listeners
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to sign in screen
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to welcome screen
                finish();
            }
        });

        passwordVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    /**
     * Toggle password visibility
     */
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPasswordVisible = false;
        } else {
            // Show password
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPasswordVisible = true;
        }
        // Keep cursor at the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    /**
     * Validate form and attempt sign up
     */
    private void attemptSignUp() {
        // Reset errors
        nameEditText.setError(null);
        phoneEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Get values
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        // Check for valid name
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            nameEditText.requestFocus();
            return;
        }

        // Check for valid phone
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone number is required");
            phoneEditText.requestFocus();
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            phoneEditText.setError("Please enter a valid phone number");
            phoneEditText.requestFocus();
            return;
        }

        // Check for valid email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        // Check for valid password
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        // Register user with UserManager
        UserManager userManager = UserManager.getInstance(this);
        
        // Check if user is already registered
        if (userManager.isUserRegistered(email)) {
            emailEditText.setError("This email is already registered");
            emailEditText.requestFocus();
            return;
        }
        
        // Register the user
        boolean success = userManager.registerUser(name, phone, email, password);
        
        if (success) {
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            navigateToMainActivity();
        } else {
            Toast.makeText(this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Validate phone number format
     */
    private boolean isValidPhoneNumber(String phone) {
        // Basic validation - could be enhanced based on specific requirements
        return phone.length() >= 10 && Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * Navigate to the main activity after successful registration
     */
    private void navigateToMainActivity() {
        // Save logged in user email to shared preferences
        String email = emailEditText.getText().toString().trim();
        getSharedPreferences("TimeSync_Login", MODE_PRIVATE)
                .edit()
                .putString("logged_in_user", email)
                .apply();
        
        Intent intent = new Intent(SignUpActivity.this, ActivitiesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish(); // Close this activity so the user can't go back to sign-up screen using back button
    }
} 