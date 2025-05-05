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

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private TextView forgotPasswordTextView;
    private TextView signUpTextView;
    private ImageButton backButton;
    private ImageButton passwordVisibilityButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        signUpTextView = findViewById(R.id.signUpTextView);
        backButton = findViewById(R.id.backButton);
        passwordVisibilityButton = findViewById(R.id.passwordVisibilityButton);

        // Set click listeners
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to forgot password screen
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                String email = emailEditText.getText().toString().trim();
                if (!email.isEmpty()) {
                    intent.putExtra("email", email);
                }
                startActivity(intent);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to sign up screen
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
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
     * Validate form and attempt login
     */
    private void attemptLogin() {
        // Reset errors
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Get values
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

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

        // Authenticate user with UserManager
        UserManager userManager = UserManager.getInstance(this);
        
        // Check if user exists
        if (!userManager.isUserRegistered(email)) {
            emailEditText.setError("This email is not registered. Please sign up first.");
            emailEditText.requestFocus();
            return;
        }
        
        // Authenticate user
        if (userManager.authenticateUser(email, password)) {
            // Authentication successful, navigate to main activity
            navigateToMainActivity();
        } else {
            // Authentication failed, show error
            passwordEditText.setError("Incorrect password");
            passwordEditText.requestFocus();
        }
    }

    /**
     * Navigate to the main activity after successful login
     */
    private void navigateToMainActivity() {
        // Save logged in user email to shared preferences
        String email = emailEditText.getText().toString().trim();
        getSharedPreferences("TimeSync_Login", MODE_PRIVATE)
                .edit()
                .putString("logged_in_user", email)
                .apply();
        
        Intent intent = new Intent(SignInActivity.this, ActivitiesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish(); // Close this activity so the user can't go back to login screen using back button
    }
} 